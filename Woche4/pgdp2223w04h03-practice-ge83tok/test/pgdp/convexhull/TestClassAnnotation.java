package pgdp.convexhull;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;

import de.tum.in.test.api.ActivateHiddenBefore;
import de.tum.in.test.api.BlacklistPackage;
import de.tum.in.test.api.BlacklistPath;
import de.tum.in.test.api.Deadline;
import de.tum.in.test.api.MirrorOutput;
import de.tum.in.test.api.PathType;
import de.tum.in.test.api.StrictTimeout;
import de.tum.in.test.api.WhitelistPath;
import de.tum.in.test.api.jupiter.Public;
import de.tum.in.test.api.localization.UseLocale;

@Public
//@PrivilegedExceptionsOnly("Eine Exception wurde unterdrückt")
@UseLocale("")
@MirrorOutput
@StrictTimeout(3)
@Deadline("2022-11-20 18:00 Europe/Berlin")
@ActivateHiddenBefore("2022-11-10 18:00 Europe/Berlin")
@WhitelistPath(value = "../pgdp2223*w04h03**", type = PathType.GLOB) // for manual assessment and development
@WhitelistPath("target") // mainly for Artemis
@BlacklistPath(value = "{test,target/test}**Test*.{java,class}", type = PathType.GLOB)
@BlacklistPackage("java.util.Arrays")
@Documented
@Retention(RUNTIME)
@Target({ TYPE, ANNOTATION_TYPE })
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public @interface TestClassAnnotation {

}
