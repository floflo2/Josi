package pgdp.trials;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;

import de.tum.in.test.api.ActivateHiddenBefore;
import de.tum.in.test.api.BlacklistPath;
import de.tum.in.test.api.Deadline;
import de.tum.in.test.api.PathType;
import de.tum.in.test.api.StrictTimeout;
import de.tum.in.test.api.WhitelistClass;
import de.tum.in.test.api.WhitelistPath;
import de.tum.in.test.api.jupiter.Public;
import de.tum.in.test.api.localization.UseLocale;

@Public
@UseLocale("")
//@MirrorOutput
@StrictTimeout(5)
@Deadline("2023-01-15 18:00 Europe/Berlin")
@ActivateHiddenBefore("2023-01-05 12:00 Europe/Berlin")
@WhitelistPath(value = "../pgdp2223*w10h01**", type = PathType.GLOB) // for manual assessment and development
@WhitelistPath("target") // mainly for Artemis
@BlacklistPath(value = "{test,target/test}**Test*.{java,class}", type = PathType.GLOB)
@WhitelistClass({ TrialOfTheGrassesBehaviorTest.class, TrialOfTheSwordBehaviorTest.class,
		TrialOfTheMountainsBehaviorTest.class, TrialOfTheDreamsBehaviorTest.class })
@Documented
@Retention(RUNTIME)
@Target({ TYPE, ANNOTATION_TYPE })
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public @interface TestClassAnnotation {

}
