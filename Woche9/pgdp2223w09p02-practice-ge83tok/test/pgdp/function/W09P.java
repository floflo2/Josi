package pgdp.function;

import de.tum.in.test.api.*;
import org.junit.jupiter.api.Test;

import java.lang.annotation.*;

@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@StrictTimeout(5)
@Documented
@MirrorOutput
@WhitelistPath(value = "../pgdp2122*w09p03**", type = PathType.GLOB) // for manual assessment and development
@WhitelistPath("target") // mainly for Artemis
@WhitelistClass({
})
@BlacklistPath(value = "{test,target/test}**Test*.{java,class}", type = PathType.GLOB)
@de.tum.in.test.api.jupiter.Public
@Test
public @interface W09P {
}

