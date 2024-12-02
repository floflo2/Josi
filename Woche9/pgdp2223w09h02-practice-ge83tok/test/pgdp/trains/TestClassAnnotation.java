package pgdp.trains;


import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import de.tum.in.test.api.*;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;

import de.tum.in.test.api.jupiter.Public;
import de.tum.in.test.api.localization.UseLocale;

@Public
@PrivilegedExceptionsOnly("Eine Exception wurde unterdrückt")
@UseLocale("")
@MirrorOutput
@StrictTimeout(10)
@Deadline("2023-01-08 18:00 Europe/Berlin")
@ActivateHiddenBefore("2022-12-15 18:00 Europe/Berlin")
@WhitelistPath(value = "../pgdp2223*w09h02**", type = PathType.GLOB) // for manual assessment and development
@WhitelistPath(value = "connections", level = PathActionLevel.DELETE)
@BlacklistPath("connections_vault")
@WhitelistPath("target") // mainly for Artemis
@BlacklistPath(value = "{test,target/test}**Test*.{java,class}", type = PathType.GLOB)
@Documented
@Retention(RUNTIME)
@Target({ TYPE, ANNOTATION_TYPE })
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public @interface TestClassAnnotation {

}
