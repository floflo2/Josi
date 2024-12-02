package pgdp.threaduins;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;

import de.tum.in.test.api.ActivateHiddenBefore;
import de.tum.in.test.api.AllowThreads;
import de.tum.in.test.api.BlacklistPath;
import de.tum.in.test.api.Deadline;
import de.tum.in.test.api.PathType;
import de.tum.in.test.api.StrictTimeout;
import de.tum.in.test.api.TrustedThreads;
import de.tum.in.test.api.TrustedThreads.TrustScope;
import de.tum.in.test.api.WhitelistClass;
import de.tum.in.test.api.WhitelistPath;
import de.tum.in.test.api.jupiter.Public;
import de.tum.in.test.api.localization.UseLocale;

@Public
//@PrivilegedExceptionsOnly("An Exception thrown")
@UseLocale("")
//@MirrorOutput
@StrictTimeout(5)
@Deadline("2023-01-29 18:00 Europe/Berlin")
@ActivateHiddenBefore("2021-10-27 18:00 Europe/Berlin")
@WhitelistPath(value = "../pgdp2223*w12h01**", type = PathType.GLOB) // for manual assessment and development
@WhitelistPath("target") // mainly for Artemis
@BlacklistPath(value = "{test,target/test}**Test*.{java,class}", type = PathType.GLOB)
@WhitelistClass({ TestSignal.class, Threaduins.class, Signal.class, ConsoleSignal.class })
@Documented
@Retention(RUNTIME)
@Target({ TYPE, ANNOTATION_TYPE })
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AllowThreads
@TrustedThreads(TrustScope.ALL_THREADS)
public @interface TestClassAnnotation {

}
