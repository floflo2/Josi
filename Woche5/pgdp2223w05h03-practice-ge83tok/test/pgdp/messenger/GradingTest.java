package pgdp.messenger;

import static org.junit.jupiter.api.Assertions.*;
import static pgdp.messenger.helper.TestHelper.*;

import java.time.*;

import org.junit.jupiter.api.*;

import de.tum.in.test.api.jupiter.*;

@W05H03
public class GradingTest {

	private static int countList = 0;
	private static boolean mergePassed = false;
	private static int countUserArray = 0;
	private static int countPinguTalk = 0;

	@DisplayName("- | List-Class Structural Test")
	@PublicTest
	@Order(1)
	void testListStructural() {
		ListTest.testListStructural();
		countList++;
	}

	@DisplayName("- | List getByID Test Public")
	@PublicTest
	@Order(2)
	void testListGetByIDPublic() {
		ListTest.testListGetByIDPublic(10, new int[] { 3, 5 });
	}

	@DisplayName("- | List megaMerge Test Public")
	@PublicTest
	@Order(3)
	void testListMegaMergePublic() {
		ListTest.testListMegaMergePublic(new int[] { 2, 2 });
	}

	@DisplayName("- | List filterDays Test Public")
	@PublicTest
	@Order(4)
	void testListFilterDaysPublic() {
		ListTest.testListFilterDaysPublic(5, LocalDateTime.of(2022, 7, 10, 0, 0), LocalDateTime.of(2022, 11, 1, 0, 0),
				false);
	}

	@DisplayName("- | List filterUser Test Public")
	@PublicTest
	@Order(5)
	void testListFilterUserPublic() {
		ListTest.testListFilterUserPublic(10, new User(77L, "Pinguin", null), 2, false);
	}

	@DisplayName("- | List toString Test Public")
	@PublicTest
	@Order(6)
	void testListToStringPublic() {
		ListTest.testListToStringPublic(3);
	}

	@DisplayName("- | List getByID Test Hidden")
	@HiddenTest
	@Order(7)
	void testListGetByIDHidden() {
		ListTest.testListGetByIDPublic(10, new int[] { 3, 5 });
		ListTest.testListGetByIDEdgeCasesHidden();
		countList++;
	}

	@DisplayName("- | List megaMerge Test Hidden")
	@HiddenTest
	@Order(8)
	void testListMegaMergeHidden() {
		ListTest.testListMegaMergePublic(new int[] { 100, 100 });
		ListTest.testListMegaMergePublic(new int[] { 5, 2, 0, 6, 10, 50, 20, 3, 70 });
		ListTest.testListMegaMergePublic(new int[] {});
		ListTest.testListMegaMergePublic(new int[] { 0 });
		ListTest.testListMegaMergePublic(new int[] { 10 });
		mergePassed = true;
	}

	@DisplayName("- | List filterDays Test Hidden")
	@HiddenTest
	@Order(9)
	void testListFilterDaysHidden() {
		ListTest.testListFilterDaysPublic(100, LocalDateTime.of(2022, 3, 10, 0, 0), LocalDateTime.of(2022, 12, 4, 0, 0),
				true);
		ListTest.testListFilterDaysPublic(100, LocalDateTime.of(2022, 7, 10, 21, 52),
				LocalDateTime.of(2022, 7, 10, 21, 53), true);
		ListTest.testListFilterDaysPublic(100, LocalDateTime.of(2022, 3, 10, 0, 0), null, true);
		ListTest.testListFilterDaysPublic(100, null, LocalDateTime.of(2022, 3, 10, 0, 0), true);
		ListTest.testListFilterDaysPublic(100, null, null, true);
		ListTest.testListFilterDaysPublic(100, LocalDateTime.of(2022, 12, 4, 0, 0), LocalDateTime.of(2022, 3, 10, 0, 0),
				true);
		ListTest.testListFilterDaysPublic(0, LocalDateTime.of(2022, 3, 10, 0, 0), LocalDateTime.of(2022, 12, 4, 0, 0),
				true);
		countList++;
	}

	@DisplayName("- | List filterUser Test Hidden")
	@HiddenTest
	@Order(10)
	void testListFilterUserHidden() {
		ListTest.testListFilterUserPublic(200, new User(77L, "Pinguin", null), 20, true);
		ListTest.testListFilterUserPublic(100, null, 20, true);
		ListTest.testListFilterUserPublic(0, new User(77L, "Pinguin", null), 3, true);
		countList++;
	}

	@DisplayName("- | List toString Test Hidden")
	@HiddenTest
	@Order(11)
	void testListToStringHidden() {
		ListTest.testListToStringPublic(200);
		ListTest.testListToStringEdgeCaseHidden();
		countList++;
	}

	@DisplayName("- | UserArray-Class Structural Test")
	@PublicTest
	@Order(12)
	void testUserArrayStructural() {
		UserArrayTest.testUserArrayStructural();
		countUserArray++;
	}

	@DisplayName("- | UserArray addUser Test Public")
	@PublicTest
	@Order(13)
	void testUserArrayAddUserPublic() {
		UserArrayTest.testUserArrayAddUserPublic();
	}

	@DisplayName("- | UserArray deleteUser Test Public")
	@PublicTest
	@Order(14)
	void testUserArrayDeleteUserPublic() {
		UserArrayTest.testUserArrayDeleteUserPublic();
	}

	@DisplayName("- | UserArray size Test Public")
	@PublicTest
	@Order(15)
	void testUserArraySizePublic() {
		UserArrayTest.testUserArraySizePublic();
	}

	@DisplayName("- | UserArray Constructor Test Hidden")
	@HiddenTest
	@Order(16)
	void testUserArrayConstructorHidden() {
		UserArrayTest.testUserArrayConstructorHidden();
		countUserArray++;
	}

	@DisplayName("- | UserArray addUser Test Hidden")
	@HiddenTest
	@Order(17)
	void testUserArrayAddUserHidden() {
		UserArrayTest.testUserArrayAddUserHidden();
		countUserArray++;
	}

	@DisplayName("- | UserArray add and delete User Test Hidden")
	@HiddenTest
	@Order(18)
	void testUserArrayAddAndDeleteHidden() {
		UserArrayTest.testUserArrayAddAndDeleteHidden();
		countUserArray++;
	}

	@DisplayName("- | UserArray size Test Hidden")
	@HiddenTest
	@Order(19)
	void testUserArraySizeHidden() {
		UserArrayTest.testUserArraySizeHidden();
		countUserArray++;
	}

	@DisplayName("- | PinguTalk-Class Structural Test")
	@PublicTest
	@Order(20)
	void testPinguTalkStructural() {
		PinguTalkTest.testPinguTalkStructural();
		countPinguTalk++;
	}

	@DisplayName("- | PinguTalk addMember Test Public")
	@PublicTest
	@Order(21)
	void testPinguTalkAddMemberPublic() {
		PinguTalkTest.testPinguTalkAddMemberPublic(10, 10);
	}

	@DisplayName("- | PinguTalk deleteMember Test Public")
	@PublicTest
	@Order(22)
	void testPinguTalkDeleteMemberPublic() {
		PinguTalkTest.testPinguTalkDeleteMemberPublic(10, 10);
	}

	@DisplayName("- | PinguTalk createNewTopic Test Public")
	@PublicTest
	@Order(23)
	void testPinguTalkCreateNewTopicPublic() {
		PinguTalkTest.testPinguTalkCreateNewTopicPublic(10, 10);
	}

	@DisplayName("- | PinguTalk deleteTopic Test Public")
	@PublicTest
	@Order(24)
	void testPinguTalkDeleteTopicPublic() {
		PinguTalkTest.testPinguTalkDeleteTopicPublic(10, 5);
	}

	@DisplayName("- | PinguTalk Constructor Test Hidden")
	@HiddenTest
	@Order(25)
	void testPinguTalkConstructorHidden() {
		PinguTalkTest.testPinguTalkConstructorHidden();
		countPinguTalk++;
	}

	@DisplayName("- | PinguTalk addMember Test Hidden")
	@HiddenTest
	@Order(26)
	void testPinguTalkAddMemberHidden() {
		PinguTalkTest.testPinguTalkAddMemberPublic(4, 200);
		countPinguTalk++;
	}

	@DisplayName("- | PinguTalk deleteMember Test Hidden")
	@HiddenTest
	@Order(27)
	void testPinguTalkDeleteMemberHidden() {
		PinguTalkTest.testPinguTalkDeleteMemberPublic(50, 100);
		countPinguTalk++;
	}

	@DisplayName("- | PinguTalk createNewTopic Test Hidden")
	@HiddenTest
	@Order(28)
	void testPinguTalkCreateNewTopicHidden() {
		PinguTalkTest.testPinguTalkCreateNewTopicPublic(50, 60);
		countPinguTalk++;
	}

	@DisplayName("- | PinguTalk deleteTopic Test Hidden")
	@HiddenTest
	@Order(29)
	void testPinguTalkDeleteTopicHidden() {
		PinguTalkTest.testPinguTalkDeleteTopicPublic(50, 100);
		countPinguTalk++;
	}

	@DisplayName("- | PinguTalk create and delete Topic Test Hidden")
	@HiddenTest
	@Order(30)
	void testPinguTalkCreateAndDeleteHidden() {
		PinguTalkTest.testPinguTalkCreateAndDeleteHidden(100, 70, 60, 80, 140);
		PinguTalkTest.testPinguTalkCreateAndDeleteHidden(100, 100, 120, 130, 170);
		countPinguTalk++;
	}

	@DisplayName("- | Grading List 1P")
	@HiddenTest
	@Order(31)
	void testGradingList1P() {
		assertTrue(countList >= 3, grading("List", countList, 3, 1));
	}

	@DisplayName("- | Grading List 2P")
	@HiddenTest
	@Order(32)
	void testGradingList2P() {
		assertTrue(countList >= 5, grading("List", countList, 5, 2));
	}

	@DisplayName("- | Grading List megaMerge")
	@HiddenTest
	@Order(33)
	void testGradingListMegaMerge() {
		assertTrue(mergePassed, "You didn't pass List.megaMerge for 1P.");
	}

	@DisplayName("- | Grading UserArray 1P")
	@HiddenTest
	@Order(34)
	void testGradingUserArray1P() {
		assertTrue(countUserArray >= 4, grading("UserArray", countUserArray, 4, 1));
	}

	@DisplayName("- | Grading UserArray 2P")
	@HiddenTest
	@Order(35)
	void testGradingUserArray2P() {
		assertTrue(countUserArray == 5, grading("UserArray", countUserArray, 5, 2));
	}

	@DisplayName("- | Grading PinguTalk 1P")
	@HiddenTest
	@Order(36)
	void testGradingPinguTalk1P() {
		assertTrue(countPinguTalk == 7, grading("PinguTalk", countPinguTalk, 7, 1));
	}

}
