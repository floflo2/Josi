package pgdp.exceptions;

public class Password {
    private final int nrUpperShould, nrLowerShould, lengthShould;
    private final char[] illegalChars;

    private static boolean matchesIllegalCharacter(
            char[] illegalChars,
            char c
    ) {
        for (char illegalChar : illegalChars)
            if (c == illegalChar) {
                return true;
            }
        return false;
    }

    public Password(int nrUpperShould, int nrLowerShould, int lengthShould, char[] illegalChars) {
        this.nrUpperShould = nrUpperShould;
        this.nrLowerShould = nrLowerShould;
        this.lengthShould = lengthShould;
        this.illegalChars = illegalChars;
    }

    public void checkFormat(String pwd) {
    }

    public void checkFormatWithLogging(String pwd) {

    }
}
