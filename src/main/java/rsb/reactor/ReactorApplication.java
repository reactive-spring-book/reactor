package rsb.reactor;

import org.springframework.aop.SpringProxy;
import org.springframework.aop.framework.Advised;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.aot.hint.TypeReference;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportRuntimeHints;
import org.springframework.core.DecoratingProxy;

import java.util.concurrent.ScheduledExecutorService;

@SpringBootApplication
@ImportRuntimeHints(ReactorApplication.Hints.class)
public class ReactorApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReactorApplication.class, args);
	}

	static class Hints implements RuntimeHintsRegistrar {

		@Override
		public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
			var prefix = "reactor.core.publisher.Traces";
			var classes = new String[] { prefix, prefix + "$StackWalkerCallSiteSupplierFactory",
					prefix + "$SharedSecretsCallSiteSupplierFactory", prefix + "$ExceptionCallSiteSupplierFactory" };
			for (var c : classes)
				hints.reflection().registerType(TypeReference.of(c), MemberCategory.values());

			hints.proxies().registerJdkProxy(ScheduledExecutorService.class, SpringProxy.class, Advised.class,
					DecoratingProxy.class);
		}

	}

}
