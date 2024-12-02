package pgdp.pingulib.datastructures.lists;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.*;

import org.junit.jupiter.api.*;

import de.tum.in.test.api.*;
import de.tum.in.test.api.jupiter.*;
import de.tum.in.test.api.localization.*;

@Public
@UseLocale("")
@MirrorOutput
@StrictTimeout(600)
@WhitelistPath(value = "../pgdp2223*w05p04**", type = PathType.GLOB) // for manual assessment and development
@WhitelistPath("target") // mainly for Artemis
@BlacklistPath(value = "{test,target/test}**Test*.{java,class}", type = PathType.GLOB)
@BlacklistPackage("java.util**")
@Documented
@Retention(RUNTIME)
@Target({ TYPE, ANNOTATION_TYPE })
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public @interface W05P04 {
	// meta annotation
}