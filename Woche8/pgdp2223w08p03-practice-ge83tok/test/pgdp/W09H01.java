package pgdp;

import de.tum.in.test.api.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@MirrorOutput
@StrictTimeout(20)
@Deadline("2022-01-10 05:30 Europe/Berlin")
@ActivateHiddenBefore("2021-12-17 15:00 Europe/Berlin")
@WhitelistPath(value = "../pgdp2021*w09h01**", type = PathType.GLOB) // for manual assessment and development
@WhitelistPath("target") // mainly for Artemis
@BlacklistPath(value = "{test,target/test}**Test*.{java,class}", type = PathType.GLOB)
@Documented
@Retention(RUNTIME)
@Target({ TYPE, ANNOTATION_TYPE })
public @interface W09H01 {
}
