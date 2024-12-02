package pgdp.messenger;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.*;

import org.junit.jupiter.api.*;

import de.tum.in.test.api.*;
import de.tum.in.test.api.jupiter.*;
import de.tum.in.test.api.localization.*;
import pgdp.messenger.helper.*;
import pgdp.messenger.mock.*;

@Public
@UseLocale("")
//@MirrorOutput
@StrictTimeout(45)
@Deadline("2022-11-27 18:00 Europe/Berlin")
@ActivateHiddenBefore("2022-11-17 17:50 Europe/Berlin")
@WhitelistPath(value = "../pgdp2223*w05h03**", type = PathType.GLOB) // for manual assessment and development
@WhitelistPath("target") // mainly for Artemis
@BlacklistPath(value = "{test,target/test}**Test*.{java,class}", type = PathType.GLOB)
@BlacklistPackage("java.util**")
@WhitelistClass({ ListTest.class, PinguTalkTest.class, UserArrayTest.class, ListHelper.class, PinguTalkHelper.class,
		TestHelper.class, UserArrayHelper.class, UserArrayMock.class, ListMock.class })
@Documented
@Retention(RUNTIME)
@Target({ TYPE, ANNOTATION_TYPE })
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public @interface W05H03 {
	// meta annotation
}