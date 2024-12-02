package pgdp.security;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.*;

import org.junit.jupiter.api.*;

import de.tum.in.test.api.*;
import de.tum.in.test.api.jupiter.*;
import de.tum.in.test.api.localization.*;

@Public
@UseLocale("")
//@MirrorOutput
@StrictTimeout(45)
@Deadline("2022-12-11 18:00 Europe/Berlin")
@ActivateHiddenBefore("2022-12-01 18:00 Europe/Berlin")
@WhitelistPath(value = "../pgdp2223*w07h02**", type = PathType.GLOB) // for manual assessment and development
@WhitelistPath("target") // mainly for Artemis
@BlacklistPath(value = "{test,target/test}**Test*.{java,class}", type = PathType.GLOB)
@WhitelistClass({ StructureHelper.class, TestHelper.class })
@Documented
@Retention(RUNTIME)
@Target({ TYPE, ANNOTATION_TYPE })
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public @interface W07H02 {
	// meta annotation
}
