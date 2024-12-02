package pgdp.array.testutils;

/**
 * @author Stephan Krusche (krusche@in.tum.de)
 * @version 3.0 (25.09.2019)
 */
public enum ScanResultType {
    CORRECTNAME_CORRECTPLACE,
    CORRECTNAME_MISPLACED,
    CORRECTNAME_MULTIPLETIMESPRESENT,
    WRONGCASE_CORRECTPLACE,
    WRONGCASE_MISPLACED,
    WRONGCASE_MULTIPLETIMESPRESENT,
    TYPOS_CORRECTPLACE,
    TYPOS_MISPLACED,
    TYPOS_MULTIPLETIMESPRESENT,
    NOTFOUND,
    UNDEFINED;
}
