package pgdp.pingunetwork;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import de.tum.in.test.api.WhitelistClass;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.opentest4j.AssertionFailedError;

import de.tum.in.test.api.dynamic.DynamicClass;
import de.tum.in.test.api.dynamic.DynamicConstructor;
import de.tum.in.test.api.dynamic.DynamicField;
import de.tum.in.test.api.dynamic.DynamicMethod;
import de.tum.in.test.api.jupiter.PublicTest;
import de.tum.in.test.api.jupiter.Hidden;
import de.tum.in.test.api.jupiter.HiddenTest;
import de.tum.in.test.api.jupiter.Public;

import static org.junit.jupiter.api.Assertions.*;

@TestClassAnnotation
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@WhitelistClass(BehaviorTests.InteractionTests.class)
@WhitelistClass(BehaviorTests.PictureTests.class)
@WhitelistClass(BehaviorTests.GroupTests.class)
@WhitelistClass(BehaviorTests.PostTests.class)
@WhitelistClass(BehaviorTests.UserTests.class)
@WhitelistClass(BehaviorTests.Grading.class)
public class BehaviorTests {

    private static final String PACKAGE_STRING = "pgdp.pingunetwork.";

    private static DynamicClass<?> User = DynamicClass.toDynamic(PACKAGE_STRING + "User");
    private static DynamicClass<?> Group = DynamicClass.toDynamic(PACKAGE_STRING + "Group");
    private static DynamicClass<?> Interaction = DynamicClass.toDynamic(PACKAGE_STRING + "Interaction");
    private static DynamicClass<?> Post = DynamicClass.toDynamic(PACKAGE_STRING + "Post");
    private static DynamicClass<?> Picture = DynamicClass.toDynamic(PACKAGE_STRING + "Picture");

    private static DynamicConstructor<?> newUser = User.constructor(String.class, String.class, Picture.toClass());
    private static DynamicConstructor<?> newGroup = Group.constructor(String.class, String.class, User.toClass(), Picture.toClass());
    private static DynamicConstructor<?> newInteraction = Interaction.constructor(User.toClass(), int.class);
    private static DynamicConstructor<?> newPost = Post.constructor(String.class, String.class);
    private static DynamicConstructor<?> newPicture = Picture.constructor(String.class, int[][].class);
    
    
    private static DynamicField<String> uName = User.field(String.class, "name");
    private static DynamicField<String> uDescription = User.field(String.class, "description");
    private static DynamicField<?> uProfilePicture = User.field(Picture.toClass(), "profilePicture");
    private static DynamicField<Object[]> uFriends = User.field(Object[].class, "friends");
    private static DynamicField<Object[]> uPosts = User.field(Object[].class, "posts");
    private static DynamicField<Object[]> uGroups = User.field(Object[].class, "groups");
    private static DynamicMethod<Void> addFriend = User.method(void.class, "addFriend", User.toClass());
    private static DynamicMethod<Void> removeFriend = User.method(void.class, "removeFriend", User.toClass());
    private static DynamicMethod<Void> joinGroup = User.method(void.class, "joinGroup", Group.toClass());
    private static DynamicMethod<Void> leaveGroup = User.method(void.class, "leaveGroup", Group.toClass());
    private static DynamicMethod<Void> interact = User.method(void.class, "interact", Post.toClass(), int.class);
    private static DynamicMethod<Void> post = User.method(void.class, "post", String.class, String.class);
    private static DynamicMethod<Void> comment = User.method(void.class, "comment", Post.toClass(), String.class, String.class);
    private static DynamicMethod<String> uGetName = User.method(String.class, "getName");
    private static DynamicMethod<String> uGetDescription = User.method(String.class, "getDescription");
    private static DynamicMethod<?> uGetProfilePicture = User.method(Picture.toClass(), "getProfilePicture");
    private static DynamicMethod<Object[]> uGetGroups = User.method(Object[].class, "getGroups");
    private static DynamicMethod<Object[]> uGetFriends = User.method(Object[].class, "getFriends");
    private static DynamicMethod<Object[]> uGetPosts = User.method(Object[].class, "getPosts");
    private static DynamicMethod<Void> uSetName = User.method(void.class, "setName", String.class);
    private static DynamicMethod<Void> uSetDescription = User.method(void.class, "setDescription", String.class);
    private static DynamicMethod<Void> uSetProfilePicture = User.method(void.class, "setProfilePicture", Picture.toClass());

    private static DynamicField<String> gName = Group.field(String.class, "name");
    private static DynamicField<String> gDescription = Group.field(String.class, "description");
    private static DynamicField<?> gOwner = Group.field(User.toClass(), "owner");
    private static DynamicField<?> gPicture = Group.field(Picture.toClass(), "picture");
    private static DynamicField<Object[]> gMembers = Group.field(Object[].class, "members");
    private static DynamicMethod<Void> addUser = Group.method(void.class, "addUser", User.toClass());
    private static DynamicMethod<Void> removeUser = Group.method(void.class, "removeUser", User.toClass());
    private static DynamicMethod<String> gGetName = Group.method(String.class, "getName");
    private static DynamicMethod<String> gGetDescription = Group.method(String.class, "getDescription");
    private static DynamicMethod<?> gGetOwner = Group.method(User.toClass(), "getOwner");
    private static DynamicMethod<?> gGetPicture = Group.method(Picture.toClass(), "getPicture");
    private static DynamicMethod<Object[]> gGetMembers = Group.method(Object[].class, "getMembers");
    private static DynamicMethod<Void> gSetName = Group.method(void.class, "setName", String.class);
    private static DynamicMethod<Void> gSetDescription = Group.method(void.class, "setDescription", String.class);
    private static DynamicMethod<Void> gSetOwner = Group.method(void.class, "setOwner", User.toClass());
    private static DynamicMethod<Void> gSetPicture = Group.method(void.class, "setPicture", Picture.toClass());
    

    private static DynamicField<?> iUser = Interaction.field(User.toClass(), "user");
    private static DynamicField<?> iType = Interaction.field(int.class, "interactionType");
    private static DynamicMethod<?> iGetUser = Interaction.method(User.toClass(), "getUser");
    private static DynamicMethod<Integer> iGetType = Interaction.method(int.class, "getInteractionType");

    private static DynamicField<String> poTitle = Post.field(String.class, "title");
    private static DynamicField<String> poContent = Post.field(String.class, "content");
    private static DynamicField<Object[]> poComments = Post.field(Object[].class, "comments");
    private static DynamicField<Object[]> poInteractions = Post.field(Object[].class, "interactions");
    private static DynamicMethod<Void> addInteraction = Post.method(void.class, "addInteraction", Interaction.toClass());
    private static DynamicMethod<Void> removeInteraction = Post.method(void.class, "removeInteraction", Interaction.toClass());
    private static DynamicMethod<Void> addComment = Post.method(void.class, "addComment", Post.toClass());
    private static DynamicMethod<Void> removeComment = Post.method(void.class, "removeComment",Post.toClass());
    private static DynamicMethod<String> poGetTitle = Post.method(String.class, "getTitle");
    private static DynamicMethod<String> poGetContent = Post.method(String.class, "getContent");
    private static DynamicMethod<Object[]> poGetComments = Post.method(Object[].class, "getComments");
    private static DynamicMethod<Object[]> poGetInteractions = Post.method(Object[].class, "getInteractions");
    private static DynamicMethod<Void> poSetTitle = Post.method(void.class, "setTitle", String.class);
    private static DynamicMethod<Void> poSetContent = Post.method(void.class, "setContent", String.class);

    private static DynamicField<String> piLocation = Picture.field(String.class, "location");
    private static DynamicField<Integer> piWidth = Picture.field(int.class, "width");
    private static DynamicField<Integer> piHeight = Picture.field(int.class, "height");
    private static DynamicField<int[][]> piData = Picture.field(int[][].class, "data");
    private static DynamicField<Object[]> piThumbnails = Picture.field(Object[].class, "thumbnails");
    private static DynamicMethod<String> piGetLocation = Picture.method(String.class, "getLocation");
    private static DynamicMethod<Integer> piGetWidth = Picture.method(int.class, "getWidth");
    private static DynamicMethod<Integer> piGetHeight = Picture.method(int.class, "getHeight");
    private static DynamicMethod<int[][]> piGetData = Picture.method(int[][].class, "getData");
    private static DynamicMethod<Object[]> piGetThumbnails = Picture.method(Object[].class, "getThumbnails");
    private static DynamicMethod<Void> piSetThumbnails = Picture.method(void.class, "setThumbnails", Array.newInstance(Picture.toClass(), 1).getClass());
    
    static Random random = new Random(69);


    // ============================================================================== //
    // -------------------------------- Public Tests -------------------------------- //
    // ============================================================================== //


    @Order(2)
    @PublicTest
    @DisplayName("Turn Green - Interaction")
    public void turnGreenInteraction() {
        assertTrue(
                iUser.exists() && iType.exists() && iGetUser.exists() && iGetType.exists(),
                "Nicht alle Member in der Klasse 'Interaction' existieren mit den geforderten Signaturen. Eventuell sind Modifier, Name und/oder Parameter noch falsch."
        );
    }

    @Order(2)
    @PublicTest
    @DisplayName("Turn Green - Picture")
    public void turnGreenPicture() {
        assertTrue(
                piData.exists() && piWidth.exists() && piHeight.exists() && piLocation.exists() && piThumbnails.exists()
                        && piGetData.exists() && piGetWidth.exists() && piGetHeight.exists() && piGetLocation.exists() && piGetThumbnails.exists()
                        && piSetThumbnails.exists(),
                "Nicht alle Member in der Klasse 'Picture' existieren mit den geforderten Signaturen. Eventuell sind Modifier, Name und/oder Parameter noch falsch."
        );
    }

    @Order(2)
    @PublicTest
    @DisplayName("Turn Green - Picture Usage")
    public void turnGreenPictureUsage() {
        assertTrue(
                uProfilePicture.exists() && uGetProfilePicture.exists() && uSetProfilePicture.exists()
                        && gPicture.exists() && gGetPicture.exists() && gSetPicture.exists(),
                "Nicht alle Member in den Klassen 'User' und 'Group', die 'Picture' verwenden, existieren mit den geforderten Signaturen. Eventuell sind Modifier, Name und/oder Parameter noch falsch."
        );
    }

    @Order(2)
    @PublicTest
    @DisplayName("Turn Green - Group removeUser")
    public void turnGreenGroupRemoveUser() {
        assertTrue(
                removeUser.exists(),
                "Die Methode 'Group.removeUser()' existiert nicht."
        );
    }

    @Order(2)
    @PublicTest
    @DisplayName("Turn Green - User interact")
    public void turnGreenUserInteract() {
        assertTrue(
                interact.exists(),
                "Die Methode 'User.interact()' existiert nicht."
        );
    }

    @Order(2)
    @PublicTest
    @DisplayName("Turn Green 1 - User post und User comment")
    public void turnGreenUserPostComment() {
        assertTrue(
                post.exists() && comment.exists(),
                "Die Methode 'User.post()' und/oder die Methode 'User.comment()' existieren nicht."
        );
    }


    // ========================================================================= //
    // -------------------------------- Grading -------------------------------- //
    // ========================================================================= //


    @Nested
    @TestClassAnnotation
    class Grading {

        @Order(2)
        @HiddenTest
        @DisplayName("Grading - Picture Usage")
        public void gradingPictureUsage() {
            // User Picture Test und Group Picture Test wurden nicht korrekt ausgeführt
            if(UserTests.userResults[7] || GroupTests.groupResults[4]) {
                fail("0/1P: Der Punkt auf die Verwendung der Klasse 'Picture' in den Klassen 'User' und 'Group' wurde nicht vergeben. Um zu verstehen, warum, sieh dir die Ergebnisse der darauf bezogenen Tests an.");
            }
            try {
            	GroupTests.groupPicture().forEach( args -> {
                    Object[] params = args.get();
                    
                    for (int i = 0; i < params.length; i++) {
                        if (params[i] instanceof Named) {
                            params[i] = ((Named<?>)params[i]).getPayload();
                        }
                    }
                    
                    (new GroupTests()).groupPicture((String)params[0], (int[][])params[1]);
                });
            	UserTests.userPicture().forEach( args -> {
                    Object[] params = args.get();
                    
                    for (int i = 0; i < params.length; i++) {
                        if (params[i] instanceof Named) {
                            params[i] = ((Named<?>)params[i]).getPayload();
                        }
                    }
                    
                    (new UserTests()).userPicture((String)params[0], (int[][])params[1]);
                });
            } catch (Throwable t) {
                fail("0/1P: Der Punkt auf die Verwendung der Klasse 'Picture' in den Klassen 'User' und 'Group' wurde nicht vergeben. Um zu verstehen, warum, sieh dir die Ergebnisse der darauf bezogenen Tests an.");
            }
        }

        @Order(2)
        @HiddenTest
        @DisplayName("Grading - Group.removeUser()")
        public void gradingGroupRemoveUser() {
            // Group Members Test wurde nicht korrekt ausgeführt
            try {
                GroupTests.groupMembers().forEach( args -> {
                    Object[] params = args.get();
                    
                    for (int i = 0; i < params.length; i++) {
                        if (params[i] instanceof Named) {
                            params[i] = ((Named<?>)params[i]).getPayload();
                        }
                    }
                    
                    (new GroupTests()).groupMembers((String)params[0], (String)params[1]);
                });
            } catch (Throwable t) {
                fail("0/1P: Der Punkt auf die Methode 'removeUser()' der Klasse 'Group' wurde nicht vergeben. Um zu verstehen, warum, sieh dir die Ergebnisse der darauf bezogenen Tests an.");
            }
        }

        @Order(2)
        @HiddenTest
        @DisplayName("Grading - User.interact()")
        public void gradingUserInteract() {
            // User Interact Test wurde nicht korrekt ausgeführt
           try {
                UserTests.userInteract().forEach( args -> {
                    Object[] params = args.get();
                    
                    for (int i = 0; i < params.length; i++) {
                        if (params[i] instanceof Named) {
                            params[i] = ((Named<?>)params[i]).getPayload();
                        }
                    }
                    
                    (new UserTests()).userInteract((int)params[0], (int)params[1]);
                });
            } catch (Throwable t) {
                fail("0/1P: Der Punkt auf die Methode 'removeUser()' der Klasse 'User' wurde nicht vergeben. Um zu verstehen, warum, sieh dir die Ergebnisse der darauf bezogenen Tests an.");
            }
        }

        @Order(2)
        @HiddenTest
        @DisplayName("Grading - User.post() und User.comment()")
        public void gradingUserPostComment() {
            // Post oder Comment wurden nicht richtig ausgeführt.
        	
        	try {
                UserTests.userPosts().forEach( args -> {
                    Object[] params = args.get();
                    
                    for (int i = 0; i < params.length; i++) {
                        if (params[i] instanceof Named) {
                            params[i] = ((Named<?>)params[i]).getPayload();
                        }
                    }
                    
                    (new UserTests()).userPosts((String)params[0], (String)params[1], (String)params[2], (String)params[3], (String)params[4], (String)params[5]);
                });
            } catch (Throwable t) {
                fail("0/1P: Der Punkt auf die Methoden 'post()' und 'comment()' der Klasse 'User' wurde nicht vergeben. Um zu verstehen, warum, sieh dir die Ergebnisse der darauf bezogenen Tests an.");
            }
        }
    	
    }
    
    


    // ============================================================================ //
    // -------------------------------- User Tests -------------------------------- //
    // ============================================================================ //


    @TestClassAnnotation
    @Nested
    class UserTests {
        
        static String[] testNames = {"Constructor Test", "Name Test", "Description Test", "Friends Test", "Posts Test", "GropsTest", "Interact Test", "Picture Test"};
        static boolean[] userResults = new boolean[testNames.length];
        
        static Stream<Arguments> userConstructor() {    
            return Stream.of(Arguments.of("Sample Name", "Sample Description", "", new int[1][1]), Arguments.of(Named.of("<Random Name>", Integer.toString(random.nextInt())), Named.of("<Random description>", Integer.toString(random.nextInt())), "", new int[1][1]));
        }
        
        static Stream<Arguments> userName() {
            return Stream.of(Arguments.of("Sample Name"), Arguments.of(Named.of("<Random Name>", Integer.toString(random.nextInt()))));
        }
        
        static Stream<Arguments> userDescription() {
            return Stream.of(Arguments.of("Sample Description"), Arguments.of(Named.of("<Random Description>", Integer.toString(random.nextInt()))));
        }
        
        static Stream<Arguments> userFriends() {
            return Stream.of(Arguments.of("Sample Friend Name 0", "Sample Friend Name 1"), Arguments.of(Named.of("<Random Friend name 0>", Integer.toString(random.nextInt())), Named.of("<Random Friend name 1>", Integer.toString(random.nextInt()))));
        }
        
        static Stream<Arguments> userPosts() {
            return Stream.of(Arguments.of("Sample Title0", "Sample Content0", "Sample Title1", "Sample Content1", "Sample Comment Title", "Sample Comment Content"), Arguments.of(Named.of("<Random Title0>", Integer.toString(random.nextInt())), Named.of("<Random Content0>", Integer.toString(random.nextInt())), Named.of("<Random Title1>", Integer.toString(random.nextInt())), Named.of("<Random Content1>", Integer.toString(random.nextInt())), Named.of("<Random Comment Title", Integer.toString(random.nextInt())), Named.of("<Random Comment Content", Integer.toString(random.nextInt()))));
        }
        
        static Stream<Arguments> userGroups() {
            return Stream.of(Arguments.of("Sample Name0", "Sample Name1"), Arguments.of(Named.of("<Random Name0>", Integer.toString(random.nextInt())), Named.of("<Random Name1>", Integer.toString(random.nextInt()))));
        }
        
        static Stream<Arguments> userInteract() {
            return Stream.of(Arguments.of(0, 1), Arguments.of(Named.of("<Random Type0>", random.nextInt()), Named.of("<Random Type1>", random.nextInt())));
        }
        
        static Stream<Arguments> userPicture() {
            return Stream.of(Arguments.of("Sample Location", new int[1][1]), Arguments.of(Named.of("<Random Location>", Integer.toString(random.nextInt())), Named.of("<Random data array>", new int[random.nextInt(100)][random.nextInt(100)])));
        }
        
        @Order(1)
        @Hidden
        @MethodSource
        @DisplayName("User - Constructor")
        @ParameterizedTest(name = "\t- {displayName} {index}: {0}, {1}")
        public void userConstructor(String name, String description, String location, int[][] data) {
            
            boolean reset = !userResults[0];
            userResults[0] = true;

            Object picture = newPicture.newInstance(location, data);
            Object user = newUser.newInstance(name, description, picture);

            assertEquals(name, uName.getOf(user), "Der Name des Nutzers wird nicht richtig gesetzt.");
            assertEquals(description, uDescription.getOf(user), "Die Beschreibung des Nutzers wird nicht richtig gesetzt.");
            assertSame(picture, uProfilePicture.getOf(user), "Das Profilbild des Nutzers wurde nicht richtig gesetzt.");
            assertArrayEquals((Object[])Array.newInstance(User.toClass(), 0), uFriends.getOf(user), "Die Freunde eines Nutzers wurden nicht richtig initialisiert.");
            assertArrayEquals((Object[])Array.newInstance(Post.toClass(), 0), uPosts.getOf(user), "Die Posts eines Nutzers wurden nicht richtig initialisiert.");
            assertArrayEquals((Object[])Array.newInstance(Group.toClass(), 0), uGroups.getOf(user), "Die Gruppen eines Nutzers wurden nicht richtig initialisiert.");
            
            if (reset) {
                userResults[0] = false;
            }
        }
        
        @Order(1)
        @Hidden
        @MethodSource
        @DisplayName("User - Name")
        @ParameterizedTest(name = "\t- {displayName} {index}: {0}")
        public void userName(String name) {
            
            boolean reset = !userResults[1];
            userResults[1] = true;
            
            String testDesc = Integer.toString(random.nextInt());
            Object picture = newPicture.newInstance("", new int[1][1]);
            Object user = newUser.newInstance("", testDesc, picture);
            Object[] friends = uFriends.getOf(user);
            Object[] posts = uPosts.getOf(user);
            Object[] groups = uGroups.getOf(user);
            
            
            uSetName.invokeOn(user, name);
            
            assertEquals(name, uName.getOf(user), "Der Setter speichert nicht den richtigen Namen.");
            assertEquals(name, uGetName.invokeOn(user), "Der Getter übergibt nicht den richtigen Namen.");
            
            assertEquals(testDesc, uDescription.getOf(user), "Die Beschreibung sollte nicht verändert sein.");
            assertSame(friends, uFriends.getOf(user), "Die Freunde sollten nicht verändert sein.");
            assertSame(groups, uGroups.getOf(user), "Die Gruppen sollten nicht verändert sein.");
            assertSame(posts, uPosts.getOf(user), "Die Posts sollten nicht verändert sein.");
            
            if (reset) {
                userResults[1] = false;
            }
        }
        
        @Order(1)
        @MethodSource
        @Hidden
        @DisplayName("User - Description")
        @ParameterizedTest(name = "\t- {displayName} {index}: {0}")
        public void userDescription(String description) {
            
            boolean reset = !userResults[2];
            userResults[2] = true;
            
            String testName = Integer.toString(random.nextInt());
            Object picture = newPicture.newInstance("", new int[1][1]);
            Object user = newUser.newInstance(testName, "", picture);
            Object[] friends = uFriends.getOf(user);
            Object[] posts = uPosts.getOf(user);
            Object[] groups = uGroups.getOf(user);
            
            
            uSetDescription.invokeOn(user, description);
            
            assertEquals(description, uDescription.getOf(user), "Der Setter speichert nicht die richtige Beschreibung.");
            assertEquals(description, uGetDescription.invokeOn(user), "Der Getter übergibt nicht die richtige Beschreibung.");
            
            assertEquals(testName, uName.getOf(user), "Der Name sollte nicht verändert sein.");
            assertSame(friends, uFriends.getOf(user), "Die Freunde sollten nicht verändert sein.");
            assertSame(groups, uGroups.getOf(user), "Die Gruppen sollten nicht verändert sein.");
            assertSame(posts, uPosts.getOf(user), "Die Posts sollten nicht verändert sein.");
            
            if (reset) {
                userResults[2] = false;
            }
        }
        
        @Order(1)
        @Hidden
        @MethodSource
        @DisplayName("User - Friends")
        @ParameterizedTest(name = "\t- {displayName} {index}: {0}, {1}")
        public void userFriends(String name0, String name1) throws IllegalArgumentException {
            
            boolean reset = !userResults[3];
            userResults[3] = true;

            String testName = Integer.toString(random.nextInt());
            String testDescription = Integer.toString(random.nextInt());
            Object picture = newPicture.newInstance("", new int[1][1]);
            Object user = newUser.newInstance(testName, testDescription, picture);
            Object pPicture = newPicture.newInstance("", new int[1][1]);
            Object friend0 = newUser.newInstance(name0, "", pPicture);
            Object friend1 = newUser.newInstance(name1, "", pPicture);
            Object[] posts = uPosts.getOf(user);
            Object[] groups = uGroups.getOf(user);
            Object[] friends = (Object[]) Array.newInstance(User.toClass(), 1);
            friends[0] = friend0;
            
            
            addFriend.invokeOn(user, friend0);
            
            assertArrayEquals(friends, uFriends.getOf(user), "Die addFriend Methode hat den Nutzer nicht richtig hinzugefügt.");
            assertArrayEquals(friends, uGetFriends.invokeOn(user), "Der Getter gibt nicht den richtigen Wert zurück.");
            
            addFriend.invokeOn(user, friend0);
            
            assertArrayEquals(friends, uFriends.getOf(user), "Der selbe Nutzer kann nicht zwei mal in einer Gruppe sein.");
            
            List<Object> cFriends = new ArrayList<Object>();
            cFriends.add(friend0);
            cFriends.add(friend1);
            
            addFriend.invokeOn(user, friend1);
            
            friends = uFriends.getOf(user);
            
            for (int i = 0; i < friends.length; i++) {
                if (cFriends.size() == 0) {
                    fail("Es sind mehr Freunde gespeichert als hinzugefügt wurden.");
                }
                if (!cFriends.remove(friends[i])) {
                    fail("Es wurde ein Freund gefunden welcher nie hinzu gefügt wurde");
                }
            }
            
            assertEquals(0, cFriends.size(), "Nicht alle hinzugefügten Freunde wurden gespeichert.");
            
            cFriends.add(friend0);
            cFriends.add(friend1);
            
            friends = uGetFriends.invokeOn(user);
            
            for (int i = 0; i < friends.length; i++) {
                if (cFriends.size() == 0) {
                    fail("Es sind mehr Freunde gespeichert als hinzugefügt wurden.");
                }
                if (!cFriends.remove(friends[i])) {
                    fail("Es wurde ein Freund gefunden welcher nie hinzu gefügt wurde");
                }
            }
            
            assertEquals(0, cFriends.size(), "Nicht alle hinzugefügten Freunde wurden gespeichert.");
            
            removeFriend.invokeOn(user, friend0);
            
            friends = (Object[]) Array.newInstance(User.toClass(), 1);
            friends[0] = friend1;
            
            assertArrayEquals(friends, uFriends.getOf(user), "Die Gespeicherten Freunde sind nicht wie erwartet." );
            assertArrayEquals(friends, uGetFriends.invokeOn(user), "Die Gespeicherten Freunde sind nicht wie erwartet." );
            
            assertEquals(testName, uName.getOf(user), "Der Name sollte nicht verändert sein.");
            assertEquals(testDescription, uDescription.getOf(user), "Die Beschreibung sollte nicht verändert sein.");
            assertSame(groups, uGroups.getOf(user), "Die Gruppen sollten nicht verändert sein.");
            assertSame(posts, uPosts.getOf(user), "Die Posts sollten nicht verändert sein.");
            
            if (reset) {
                userResults[3] = false;
            }
        }
        
        @Order(1)
        @Hidden
        @MethodSource
        @DisplayName("User - Posts")
        @ParameterizedTest(name = "\t- {displayName} {index}: {0}, {2}, {4}")
        public void userPosts(String title0, String content0, String title1, String content1, String commentTitle, String commentContent) throws IllegalArgumentException {
            
            boolean reset = !userResults[4];
            userResults[4] = true;

            String testName = Integer.toString(random.nextInt());
            String testDescription = Integer.toString(random.nextInt());
            Object picture = newPicture.newInstance("", new int[1][1]);
            Object user = newUser.newInstance(testName, testDescription, picture);
            Object[] friends = uFriends.getOf(user);
            Object[] groups = uGroups.getOf(user);
            
            
            post.invokeOn(user, title0, content0);
            
            Object[] posts = uPosts.getOf(user);
            assertEquals(1, posts.length, "Die Menge an gespeicherten Posts ist nicht wie erwartet.");
            Object post0 = posts[0];

            assertEquals(title0, poTitle.getOf(posts[0]), "Der gespeicherte Post hat nicht den übergebenen Titel");
            assertEquals(content0, poContent.getOf(posts[0]), "Der gespeicherte Post hat nicht den übergebenen Inhalt");
            
            post.invokeOn(user, title1, content1);
            
            List<String[]> cPosts = new ArrayList<>();
            cPosts.add(new String[] {title0, content0});
            cPosts.add(new String[] {title1, content1});
            
            posts = uPosts.getOf(user);
            
            for (int i = 0; i < posts.length; i++) {
                
                boolean found = false;
                
                for (int j = 0; j < cPosts.size() && !found; j++) {
                    
                    if (poTitle.getOf(posts[i]).equals(cPosts.get(j)[0])) {
                        
                        assertEquals(cPosts.get(j)[1], poContent.getOf(posts[i]), "Der Inhalt des gespeicherten Posts passt nicht zum Titel.");
                    }
                    cPosts.remove(j);
                    found = true;
                }
                
                if(!found) {
                    fail("Post found that never was posted.");
                }
            }
            
            if (cPosts.size() != 0) {
                fail("Not all posted posts were stored.");
            }
            
            cPosts.add(new String[] {title0, content0});
            cPosts.add(new String[] {title1, content1});
            
            posts = uGetPosts.invokeOn(user);
            
            for (int i = 0; i < posts.length; i++) {
                
                boolean found = false;
                
                for (int j = 0; j < cPosts.size() && !found; j++) {
                    
                    if (poTitle.getOf(posts[i]).equals(cPosts.get(j)[0])) {
                        
                        assertEquals(cPosts.get(j)[1], poContent.getOf(posts[i]), "Der Inhalt des gespeicherten Posts passt nicht zum Titel.");
                    }
                    cPosts.remove(j);
                    found = true;
                }
                
                if(!found) {
                    fail("Post found that never was posted.");
                }
            }
            
            if (cPosts.size() != 0) {
                fail("Not all posted posts were stored.");
            }
            
            comment.invokeOn(user, post0, commentTitle, commentContent);
            
            
            cPosts.add(new String[] {title0, content0});
            cPosts.add(new String[] {title1, content1});
            cPosts.add(new String[] {commentTitle, commentContent});
            
            posts = uPosts.getOf(user);
            
            for (int i = 0; i < posts.length; i++) {
                
                boolean found = false;
                
                for (int j = 0; j < cPosts.size() && !found; j++) {
                    
                    if (poTitle.getOf(posts[i]).equals(cPosts.get(j)[0])) {
                        
                        assertEquals(cPosts.get(j)[1], poContent.getOf(posts[i]), "Der Inhalt des gespeicherten Posts passt nicht zum Titel.");
                    }
                    cPosts.remove(j);
                    found = true;
                }
                
                if(!found) {
                    fail("Post found that never was posted.");
                }
            }
            
            if (cPosts.size() != 0) {
                fail("Not all posted posts were stored.");
            }
            
            
            cPosts.add(new String[] {title0, content0});
            cPosts.add(new String[] {title1, content1});
            cPosts.add(new String[] {commentTitle, commentContent});
            
            posts = uGetPosts.invokeOn(user);
            
            for (int i = 0; i < posts.length; i++) {
                
                boolean found = false;
                
                for (int j = 0; j < cPosts.size() && !found; j++) {
                    
                    if (poTitle.getOf(posts[i]).equals(cPosts.get(j)[0])) {
                        
                        assertEquals(cPosts.get(j)[1], poContent.getOf(posts[i]), "Der Inhalt des gespeicherten Posts passt nicht zum Titel.");
                    }
                    cPosts.remove(j);
                    found = true;
                }
                
                if(!found) {
                    fail("Post found that never was posted.");
                }
            }
            
            if (cPosts.size() != 0) {
                fail("Not all posted posts were stored.");
            }
            
            
            assertEquals(1, poComments.getOf(post0).length);
            
            Object comment = poComments.getOf(post0)[0];

            assertEquals(commentTitle, poTitle.getOf(comment), "Der Kommentar wurde nicht beim zugehörgien Post gespeicher.");
            assertEquals(commentContent, poContent.getOf(comment), "Der Kommentar wurde nicht beim zugehörgien Post gespeicher.");
            
            assertEquals(testName, uName.getOf(user), "Der Name sollte nicht verändert sein.");
            assertEquals(testDescription, uDescription.getOf(user), "Die Beschreibung sollte nicht verändert sein.");
            assertSame(groups, uGroups.getOf(user), "Die Gruppen sollten nicht verändert sein.");
            assertSame(friends, uFriends.getOf(user), "Die Freunde sollten nicht verändert sein.");
            
            if (reset) {
                userResults[4] = false;
            }
        }
        
        @Order(1)
        @Hidden
        @MethodSource
        @DisplayName("User - Groups")
        @ParameterizedTest(name = "\t- {displayName} {index}: {0}, {1}")
        public void userGroups(String name0, String name1) throws IllegalArgumentException {
            
            boolean reset = !userResults[5];
            userResults[5] = true;

            String testName = Integer.toString(random.nextInt());
            String testDescription = Integer.toString(random.nextInt());
            Object picture = newPicture.newInstance("", new int[1][1]);   
            Object uPicture = newPicture.newInstance("", new int[1][1]);
            Object owner = newUser.newInstance("Owner", "Owner", uPicture);
            Object user = newUser.newInstance(testName, testDescription, picture);
            Object group0 = newGroup.newInstance(name0, "", owner, uPicture);
            Object group1 = newGroup.newInstance(name1, "", owner, uPicture);
            Object[] posts = uPosts.getOf(user);
            Object[] friends = uFriends.getOf(user);
            Object[] groups = (Object[]) Array.newInstance(Group.toClass(), 1);
            groups[0] = group0;
            
            List<Object> cMembers = new ArrayList<Object>();
            cMembers.add(user);
            cMembers.add(owner);
            
            joinGroup.invokeOn(user, group0);
            
            Object[] members = gMembers.getOf(group0);
            
            for (int i = 0; i < members.length; i++) {
                if (cMembers.size() == 0) {
                    fail("Es sind mehr Mitglieder in der Gruppe gespeichert als beigetreten sind.");
                }
                if (!cMembers.remove(members[i])) {
                    fail("Es wurde eine Mitglied einer Gruppe gefunden welches nie beigetreten ist.");
                }
            }
            
            if (cMembers.size() != 0) {
                fail("Nicht alle hinzugefügten Mitglieder wurden gespeichert.");
            }
            
            
            assertArrayEquals(groups, uGroups.getOf(user), "Die gespeicherten Gruppen sind nicht wie erwartet.");
            assertArrayEquals(groups, uGetGroups.invokeOn(user), "Die gespeicherten Gruppen sind nicht wie erwartet.");
            
            joinGroup.invokeOn(user, group0);
            
            cMembers.add(owner);
            cMembers.add(user);
            
            members = gMembers.getOf(group0);
            
            for (int i = 0; i < members.length; i++) {
                if (cMembers.size() == 0) {
                    fail("Es sind mehr Mitglieder in der Gruppe gespeichert als beigetreten sind.");
                }
                if (!cMembers.remove(members[i])) {
                    fail("Es wurde eine Mitglied einer Gruppe gefunden welches nie beigetreten ist");
                }
            }
            
            if (cMembers.size() != 0) {
                fail("Nicht alle hinzugefügten Mitglieder wurden gespeichert.");
            }
            
            assertArrayEquals(groups, uGroups.getOf(user), "Die gespeicherten Gruppen sind nicht wie erwartet.");
            assertArrayEquals(groups, uGetGroups.invokeOn(user), "Die gespeicherten Gruppen sind nicht wie erwartet.");
            
            assertArrayEquals(members, gMembers.getOf(group0), "Die gespeicherten Nutzer sind nicht wie erwartet.");
            
            List<Object> cGroups = new ArrayList<Object>();
            cGroups.add(group0);
            cGroups.add(group1);
            
            joinGroup.invokeOn(user, group1);
            
            cMembers.add(owner);
            cMembers.add(user);
            
            members = gMembers.getOf(group1);
            
            for (int i = 0; i < members.length; i++) {
                if (cMembers.size() == 0) {
                    fail("Es sind mehr Mitglieder in der Gruppe gespeichert als beigetreten sind.");
                }
                if (!cMembers.remove(members[i])) {
                    fail("Es wurde eine Mitglied einer Gruppe gefunden welches nie beigetreten ist");
                }
            }
            
            if (cMembers.size() != 0) {
                fail("Nicht alle hinzugefügten Mitglieder wurden gespeichert.");
            }

            assertArrayEquals(members, gMembers.getOf(group0), "Die gespeicherten Nutzer sind nicht wie erwartet.");
            assertArrayEquals(members, gMembers.getOf(group1), "Die gespeicherten Nutzer sind nicht wie erwartet.");
            
            groups = uGroups.getOf(user);
            
            for (int i = 0; i < groups.length; i++) {
                if (cGroups.size() == 0) {
                    fail("Es sind mehr Gruppen gespeichert als beigetreten wurde.");
                }
                if (!cGroups.remove(groups[i])) {
                    fail("Es wurde eine Gruppe gefunden welcher nie beigetreten wurde");
                }
            }
            
            if (cGroups.size() != 0) {
                fail("Nicht alle hinzugefügten Freunde wurden gespeichert.");
            }
            
            cGroups.add(group0);
            cGroups.add(group1);
            
            groups = uGetGroups.invokeOn(user);
            
            for (int i = 0; i < groups.length; i++) {
                if (cGroups.size() == 0) {
                    fail("Es sind mehr Gruppen gespeichert als beigetreten wurde.");
                }
                if (!cGroups.remove(groups[i])) {
                    fail("Es wurde eine Gruppe gefunden welcher nie beigetreten wurde");
                }
            }
            
            if (cGroups.size() != 0) {
                fail("Nicht alle hinzugefügten Freunde wurden gespeichert.");
            }
            
            leaveGroup.invokeOn(user, group0);
            
            groups = (Object[]) Array.newInstance(Group.toClass(), 1);
            groups[0] = group1;
            
            assertArrayEquals(groups, uGroups.getOf(user), "Die gespeicherten Gruppen sind nicht wie erwartet.");
            assertArrayEquals(groups, uGetGroups.invokeOn(user), "Die gespeicherten Gruppen sind nicht wie erwartet.");
            
            members = (Object[]) Array.newInstance(User.toClass(),1);
            members[0] = owner;
            
            assertArrayEquals(members, gMembers.getOf(group0), "Die gespeicherten Nutzer sind nicht wie erwartet.");
            
            assertEquals(testName, uName.getOf(user), "Der Name sollte nicht verändert sein.");
            assertEquals(testDescription, uDescription.getOf(user), "Die Beschreibung sollte nicht verändert sein.");
            assertSame(posts, uPosts.getOf(user), "Die Posts sollten nicht verändert sein.");
            assertSame(friends, uFriends.getOf(user), "Die Freunde sollten nicht verändert sein.");
            
            if (reset) {
                userResults[5] = false;
            }
        }
        
        @Order(1)
        @Hidden
        @MethodSource
        @DisplayName("User - interact")
        @ParameterizedTest(name = "\t- {displayName} {index}: {0}, {1}")
        public void userInteract(int type0, int type1) throws IllegalArgumentException {
            boolean reset = !userResults[6];
            userResults[6] = true;
            
            
            Object post = newPost.newInstance("Post Title", "Post Content");
            Object uPicture = newPicture.newInstance("", new int[1][1]);
            Object user = newUser.newInstance("User Name", "User Description", uPicture);
            
            interact.invokeOn(user, post, type0);
            
            assertEquals(1, poInteractions.getOf(post).length, "Es ist nicht die erwartete Menge an Interacktionen gespeichert");
            Object interaction = poInteractions.getOf(post)[0];
            assertEquals(type0, iType.getOf(interaction), "Der Typ der Interaktion ist nicht wie erwartet");
            assertSame(user, iUser.getOf(interaction), "Der Nutzer der Interaktion ist nicht wie erwartet");
            
            interact.invokeOn(user, post, type1);
            
            List<Integer> cInteractions = new ArrayList<>();
            cInteractions.add(type0);
            cInteractions.add(type1);
            
            
            Object[] interactions = poInteractions.getOf(post);
            
            for (int i = 0; i < interactions.length; i++) {
                
                if (!cInteractions.contains(iType.getOf(interactions[i]))) {
                    fail("Interaqktion mit unbekanntem Typ wurde gespeichert.");
                }
                assertSame(user, iUser.getOf(interactions[i]), "Der Nutzer einer Interaktion passt nicht zum Typ.");
                cInteractions.remove(iType.getOf(interactions[i]));
                
            }
            
            if (reset) {
                userResults[6] = false;
            }
        }

        @Order(1)
        @Hidden
        @MethodSource
        @DisplayName("User - Picture")
        @ParameterizedTest(name = "\t- {displayName} {index}: {0}, {1}")
        public void userPicture(String location, int data[][]) throws IllegalArgumentException {
            
            boolean reset = !userResults[7];
            userResults[7] = true;

            String testName = Integer.toString(random.nextInt());
            String testDescription = Integer.toString(random.nextInt());
            Object uPicture = newPicture.newInstance("", new int[1][1]);
            Object user = newUser.newInstance(testName, testDescription, uPicture);
            Object[] posts = uPosts.getOf(user);
            Object[] groups = uGroups.getOf(user);
            Object[] friends = uFriends.getOf(user);

            assertSame(uPicture, uProfilePicture.getOf(user));
            
            Object picture = newPicture.newInstance(location, data);
            
            uSetProfilePicture.invokeOn(user, picture);
            
            assertSame(picture, uProfilePicture.getOf(user), "Das Profilbild wurde nicht richtig gesetzt");
            assertSame(picture, uGetProfilePicture.invokeOn(user), "Der Getter übergibt nicht den richtigen Wert.");
            
            assertEquals(testName, uName.getOf(user), "Der Name ist nicht wie erwartet.");
            assertEquals(testDescription, uDescription.getOf(user), "Die Beschreibung ist nicht wie erwartet.");
            assertSame(friends, uFriends.getOf(user), "Die Freunde sind nicht wie erwartet.");
            assertSame(posts, uPosts.getOf(user), "Die Posts sind nicht wie erwartet.");
            assertSame(groups, uGroups.getOf(user), "Die Gruppen sind nicht wie erwartet.");
            
            if (reset) {
                userResults[7] = false;
            }
        } 
        
        @Order(1)
        @Public
        @Test
        @DisplayName("Turn Green 2 - User post und User comment")
        public void userPublic() {
            
            try {
                UserTests.userConstructor().forEach( args -> {
                    Object[] params = args.get();
                    
                    for (int i = 0; i < params.length; i++) {
                        if (params[i] instanceof Named) {
                            params[i] = ((Named<?>)params[i]).getPayload();
                        }
                    }
                    
                    userConstructor((String)params[0], (String)params[1], (String)params[2], (int[][])params[3]);
                });
            } catch (AssertionFailedError e) {
                fail("Der User Konstruktor funktioniert nicht wie erwartet.");
            } catch (Exception e) {
                fail("Eine Exception ist aufgetreten.");
            }
            
            try {
                UserTests.userPosts().forEach( args -> {
                    
                    Object[] params = args.get();
                    
                    for (int i = 0; i < params.length; i++) {
                        if (params[i] instanceof Named) {
                            params[i] = ((Named<?>)params[i]).getPayload();
                        }
                    }
                    
                    try {
                        userPosts((String) params[0], (String) params[1],(String) params[2],(String) params[3],(String) params[4], (String) params[5]);
                    } catch (Exception e) {
                        fail("Eine Exception ist aufgetreten.");
                    } 
                });
            } catch (AssertionFailedError e) {
                fail("Die Post oder Comment Methode funktioniert nicht wie erwartet.");
            } catch (Exception e) {
                fail("Eine Exception ist aufgetreten");
            }
        }
        
        @Order(2)
        @HiddenTest
        public void userSummary() {
            
            boolean allGood = true;
            
            StringBuilder result = new StringBuilder("Folgende Tests für Interactions sind fehlgeschlagen:\n");
            
            for (int i = 0; i < userResults.length; i++) {
                if (userResults[i]) {
                    allGood = false;
                    result.append(testNames[i] + "\n");
                }
            }
            
            if (!allGood) {
                fail(result.toString());
            }

        }
    }


    // ============================================================================ //
    // -------------------------------- Post Tests -------------------------------- //
    // ============================================================================ //

    
    @Nested
    @Hidden
    @TestClassAnnotation
    class PostTests {
        
        static String[] testNames = {"Constructor Test", "Title Test", "Content Test", "Comments Test", "Interactions Test"};
        static boolean[] postResults = new boolean[testNames.length];
        
        private static Stream<Arguments> postConstructor() {
            return Stream.of(Arguments.of("Sample Title", "Sample Content"), Arguments.of(Named.of("<Random Title>", Integer.toString(random.nextInt())), Named.of("<Random Content>", Integer.toString(random.nextInt()))));
        }
        
        private static Stream<Arguments> postTitle() {
            return Stream.of(Arguments.of("Sample Title"), Arguments.of(Named.of("<Random Title>", Integer.toString(random.nextInt()))));
        }
        
        private static Stream<Arguments> postContent() {
            return Stream.of(Arguments.of("Sample Content"), Arguments.of(Named.of("<Random Content>", Integer.toString(random.nextInt()))));
        }
        
        private static Stream<Arguments> postComments() {
            return Stream.of(Arguments.of("Sample Title0", "Sample Content0", "Sample Title1", "Sample Content1"), Arguments.of(Named.of("<Random Title0>", Integer.toString(random.nextInt())), Named.of("<Random Content0>", Integer.toString(random.nextInt())), Named.of("<Random Title1>", Integer.toString(random.nextInt())), Named.of("<Random Content1>", Integer.toString(random.nextInt()))));
        }
        
        private static Stream<Arguments> postInteractions() {
            return Stream.of(Arguments.of(0,1), Arguments.of(Named.of("<Random Type0>", random.nextInt()), Named.of("<Random Type1>", random.nextInt())));
        }
        

        @Order(1)
        @DisplayName("Post - Constructor")
        @ParameterizedTest(name = "\t- {displayName} {index}: {0}, {1}")
        @MethodSource
        public void postConstructor(String title, String content) {

            boolean reset = !postResults[0];
            postResults[0] = true;
            
            Object post = newPost.newInstance(title, content);
            
            assertEquals(title, poTitle.getOf(post), "Der Titel wird nicht richtig gesetzt.");
            assertEquals(content, poContent.getOf(post), "Der Inhalt wird nicht richtig gesetzt.");
            assertArrayEquals((Object[])Array.newInstance(Post.toClass(), 0), poComments.getOf(post), "Die Kommentare werden nicht richtig initialisiert.");
            assertArrayEquals((Object[])Array.newInstance(Interaction.toClass(), 0), poInteractions.getOf(post), "Die Interaktionen werden nicht richtig initialsiert.");
            
            if (reset) {
                postResults[0] = false;
            }
        }

        @Order(1)
        @DisplayName("Post - Title")
        @ParameterizedTest(name = "\t- {displayName} {index}: {0}")
        @MethodSource
        public void postTitle(String title) {

            boolean reset = !postResults[1];
            postResults[1] = true;
            
            String testDesc = Integer.toString(random.nextInt());
            Object post = newPost.newInstance("", testDesc);
            Object[] interactions = poInteractions.getOf(post);
            Object[] comments = poComments.getOf(post);
            
            poSetTitle.invokeOn(post, title);
            assertEquals(title, poTitle.getOf(post), "Der Setter setzt den Titel nicht richtig.");
            assertEquals(title, poGetTitle.invokeOn(post), "Der Getter gibt nicht den richtigen Wert zurück.");
            
            assertSame(comments, poComments.getOf(post), "Die Kommentare sollten nicht verändert werden.");
            assertSame(interactions, poInteractions.getOf(post), "Die Kommentare sollten nicht verändertwerden.");
            
            if (reset) {
                postResults[1] = false;
            }
        }

        @Order(1)
        @DisplayName("Post - Content")
        @ParameterizedTest(name = "\t- {displayName} {index}: {0}")
        @MethodSource
        public void postContent(String content) {

            boolean reset = !postResults[2];
            postResults[2] = true;
            
            String testTitle = Integer.toString(random.nextInt());
            Object post = newPost.newInstance(testTitle, "");
            Object[] interactions = poInteractions.getOf(post);
            Object[] comments = poComments.getOf(post);
            
            poSetContent.invokeOn(post, content);
            assertEquals(content, poContent.getOf(post), "Der Setter setzt den Inhalt nicht richtig.");
            assertEquals(content, poGetContent.invokeOn(post), "Der Getter gibt den falschen Wert zurück.");
            
            assertSame(comments, poComments.getOf(post), "Die Kommentare sollten nicht verändert werdeen.");
            assertSame(interactions, poInteractions.getOf(post), "Die Interaktionen sollten nicht verändert sein.");
            
            if (reset) {
                postResults[2] = false;
            }
        }

        @Order(1)
        @DisplayName("Post - Comments")
        @ParameterizedTest(name = "\t- {displayName} {index}: {0}, {2}")
        @MethodSource
        public void postComments(String title0, String content0, String title1, String content1) {

            boolean reset = !postResults[3];
            postResults[3] = true;

            String testTitle = Integer.toString(random.nextInt());
            String testContent = Integer.toString(random.nextInt());
            Object post = newPost.newInstance(testTitle, testContent);
            Object comment0 = newPost.newInstance(title0, content0);
            Object comment1 = newPost.newInstance(title1, content1);
            Object[] interactions = poInteractions.getOf(post);
            
            Object[] comments = (Object[]) Array.newInstance(Post.toClass(), 1);
            comments[0] = comment0;
            
            addComment.invokeOn(post, comment0);
            
            assertArrayEquals(comments, poComments.getOf(post), "Der Kommentar wurde nicht richtig zhinzugefügt.");
            assertArrayEquals(comments, poGetComments.invokeOn(post), "Der Getter gibt nicht den richtigen Wert zurück.");
            
            addComment.invokeOn(post, comment0);
            
            assertArrayEquals(comments, poComments.getOf(post), "Der Nutzer soll nicht doppelt hinzugefügt werden.");
            assertArrayEquals(comments, poGetComments.invokeOn(post), "Der Getter gibt nicht den richtigen Wert zurück.");
            
            addComment.invokeOn(post, comment1);
            
            List<Object> cComments = new ArrayList<>();
            cComments.add(comment0);
            cComments.add(comment1);
            
            comments = poComments.getOf(post);
            
            for (int i = 0; i < comments.length; i++) {
                
                if (cComments.size() == 0) {
                    fail("Es wurden mehr Kommentare gespeichert als gepostet wurden.");
                }
                if (!cComments.remove(comments[i])) {
                    fail("Der gespeicherte Kommentar wurde nie gepostet.");
                }
            }
            
            assertEquals(0, cComments.size(), "Es wurden nicht alle hinzugefügten Kommentare gespeichert.");
            

            cComments.add(comment0);
            cComments.add(comment1);
            
            comments = poGetComments.invokeOn(post);
            
            for (int i = 0; i < comments.length; i++) {
                
                if (cComments.size() == 0) {
                    fail("Es wurden mehr Kommentare gespeichert als gepostet wurden.");
                }
                if (!cComments.remove(comments[i])) {
                    fail("Der gespeicherte Kommentar wurde nie gepostet.");
                }
            }
            
            assertEquals(0, cComments.size(), "Es wurden nicht alle hinzugefügten Kommentare gespeichert.");
            
            removeComment.invokeOn(post, comment0);
            comments = (Object[]) Array.newInstance(Post.toClass(), 1);
            comments[0] = comment1;

            assertArrayEquals(comments, poComments.getOf(post), "Der Kommentar wurde nicht richtig entfernt.");
            assertArrayEquals(comments, poGetComments.invokeOn(post), "Der getter gibt nicht den Richtigen wert zurück");
            
            assertEquals(testTitle, poTitle.getOf(post), "Der Titel sollte sich nicht ändern.");
            assertEquals(testContent, poContent.getOf(post), "Der Inhalt sollte sich nicht ändern.");
            assertSame(interactions, poInteractions.getOf(post), "Die Interaktionen sollten sich nicht ändern");
            
            if (reset) {
                postResults[3] = false;
            }
        }

        @Order(1)
        @DisplayName("Post - Interactions")
        @ParameterizedTest(name = "\t- {displayName} {index}: {0}, {1}")
        @MethodSource
        public void postInteractions(int type0, int type1) {

            boolean reset = !postResults[4];
            postResults[4] = true;

            String testTitle = Integer.toString(random.nextInt());
            String testContent = Integer.toString(random.nextInt());
            Object post = newPost.newInstance(testTitle, testContent);
            Object sUser = newUser.newInstance("","",newPicture.newInstance("", new int[1][1]));
            Object interaction0 = newInteraction.newInstance(sUser, type0);
            Object interaction1 = newInteraction.newInstance(sUser, type1);
            Object[] comments = poComments.getOf(post);
            
            Object[] interactions = (Object[]) Array.newInstance(Interaction.toClass(), 1);
            interactions[0] = interaction0;
            
            addInteraction.invokeOn(post, interaction0);
            
            assertArrayEquals(interactions, poInteractions.getOf(post), "Die Interaktion wird nicht richtig hinzugefügt.");
            assertArrayEquals(interactions, poGetInteractions.invokeOn(post), "Der Getter gibt nicht den richtigen Wert zurück.");
            
            addInteraction.invokeOn(post, interaction0);
            
            assertArrayEquals(interactions, poInteractions.getOf(post), "Die Interaktion sollte nicht doppelt hinzugefügt werden.");
            assertArrayEquals(interactions, poGetInteractions.invokeOn(post), "Der Getter gibt nicht den richtigen Wert zurück.");
            
            addInteraction.invokeOn(post, interaction1);
            
            List<Object> cInteractions = new ArrayList<>();
            cInteractions.add(interaction0);
            cInteractions.add(interaction1);
            
            interactions = poInteractions.getOf(post);
            
            for (int i = 0; i < interactions.length; i++) {
                
                if (cInteractions.size() == 0) {
                    fail("Es wurden mehr Interaktionen gespeichert als gepostet wurden.");
                }
                if (!cInteractions.remove(interactions[i])) {
                    fail("Die gespeicherte Interaktion wurde nie gepostet.");
                }
            }
            
            assertEquals(0, cInteractions.size(), "Es wurden nicht alle Interaktionen gespeichert");
            

            cInteractions.add(interaction0);
            cInteractions.add(interaction1);
            
            interactions = poGetInteractions.invokeOn(post);
            
            for (int i = 0; i < interactions.length; i++) {
                
                if (cInteractions.size() == 0) {
                    fail("Es wurden mehr Interaktionen gespeichert als gepostet wurden.");
                }
                if (!cInteractions.remove(interactions[i])) {
                    fail("Die gespeicherte Interaktion wurde nie gepostet.");
                }
            }
            
            assertEquals(0, cInteractions.size(), "Es wurden nicht alle Interaktionen gespeichert");
            
            removeInteraction.invokeOn(post, interaction0);
            interactions = (Object[]) Array.newInstance(Interaction.toClass(), 1);
            interactions[0] = interaction1;

            assertArrayEquals(comments, poComments.getOf(post), "Die Interaktion wurde nicht richtig entfernt.");
            assertArrayEquals(comments, poGetComments.invokeOn(post), "Der getter übergibt nicht den richtigen Wert.");
            
            assertEquals(testTitle, poTitle.getOf(post), "Der Titel sollte sich nicht verändern.");
            assertEquals(testContent, poContent.getOf(post), "Der Inhalt sollte sich nicht verändern.");
            assertSame(comments, poComments.getOf(post), "Die Kommentare sollten sich nicht verändern.");
            
            if (reset) {
                postResults[4] = false;
            }
        }
        
        @Order(1)
        // @PublicTest
        @DisplayName("Post Public Test")
        public void postPublic() {        
            
            try {
                PostTests.postConstructor().forEach( args -> {
                    Object[] params = args.get();
                    
                    for (int i = 0; i < params.length; i++) {
                        if (params[i] instanceof Named) {
                            params[i] = ((Named<?>)params[i]).getPayload();
                        }
                    }
                    postConstructor((String)params[0], (String)params[1]);
                });
            } catch (AssertionFailedError e) {
                fail("Der Post Constructor funktioniert nicht wie erwartet.");
            } catch (Exception e) {
                fail("Eine Exception ist aufgetreten");
            }
            
        }
        
        @Order(2)
        @HiddenTest
        public void postSummary() {
            
            boolean allGood = true;
            
            StringBuilder result = new StringBuilder("Folgende Tests für Posts sind fehlgeschlagen:\n");
            
            for (int i = 0; i < postResults.length; i++) {
                if (postResults[i]) {
                    allGood = false;
                    result.append(testNames[i] + "\n");
                }
            }
            
            if (!allGood) {
                fail(result.toString());
            }

        }
    }


    // ============================================================================= //
    // -------------------------------- Group Tests -------------------------------- //
    // ============================================================================= //


    @Nested
    @TestClassAnnotation
    class GroupTests {
        
        static String[] testNames = {"Constructor Test", "Name Test", "Description Test", "Owner Test", "Picture Test", "Member Test"};
        static boolean[] groupResults = new boolean[testNames.length];
        
        private static Stream<Arguments> groupConstructor() {
            return Stream.of(Arguments.of("Sample Name", "SampleDescription", new Object[] {newUser.newInstance("Sample User Name", "Sample User Description", newPicture.newInstance("", new int[1][1])),  newPicture.newInstance("Sample Location", new int[1][1])}),
                    Arguments.of(Named.of("<Random Name>", Integer.toString(random.nextInt())), Named.of("<Random Description>", Integer.toString(random.nextInt())), new Object[] {newUser.newInstance(Integer.toString(random.nextInt()), Integer.toString(random.nextInt()),  newPicture.newInstance("", new int[1][1])), newPicture.newInstance(Integer.toString(random.nextInt()), new int[random.nextInt(100)][random.nextInt(100)])}));
        }
        
        private static Stream<Arguments> groupName() {
            return Stream.of(Arguments.of("Sample Name"), Arguments.of(Named.of("<Random Name>", Integer.toString(random.nextInt()))));
        }
        
        private static Stream<Arguments> groupDescription() {
            return Stream.of(Arguments.of("Sample Description"), Arguments.of(Named.of("<Random Description>", Integer.toString(random.nextInt()))));
        }
        
        private static Stream<Arguments> groupOwner() {
            return Stream.of(Arguments.of("Sample Name", "Sample Description"), Arguments.of(Named.of("<Random Name>", Integer.toString(random.nextInt())), Named.of("<Random Description>", Integer.toString(random.nextInt()))));
        }
        
        private static Stream<Arguments> groupPicture() {
            return Stream.of(Arguments.of("Sample Location", Named.of("Sample data", new int[1][1])), Arguments.of(Named.of("<Random Location>", Integer.toString(random.nextInt())), Named.of("<Random data>", new int[random.nextInt(100)][random.nextInt(100)])));
        }
        
        private static Stream<Arguments> groupMembers() {
            return Stream.of(Arguments.of("Sample Name0", "Sample Name1"), Arguments.of(Named.of("<Random Name0>", Integer.toString(random.nextInt())), Named.of("<Random Name1>", Integer.toString(random.nextInt()))));
        }

        @Order(1)
        @DisplayName("Group - Constructor")
        @ParameterizedTest(name = "\t- {displayName} {index}: {0}, {1}")
        @MethodSource
        @Hidden
        public void groupConstructor(String name, String description, Object[] objs) {
            
            boolean reset = !groupResults[0];
            groupResults[0] = true;
            
            Object picture = objs[1];
            Object owner = objs[0];
            Object group = newGroup.newInstance(name, description, owner, picture);
            
            assertEquals(name, gName.getOf(group), "Der Name wurde nicht richtig gesetzt.");
            assertEquals(description, gDescription.getOf(group), "Die Beschreibung wurde nicht richtig gesetzt.");
            assertSame(owner, gOwner.getOf(group), "Der Besitzer wurde nicht richtig gesetzt.");
            assertSame(picture, gPicture.getOf(group), "Das Profil wurde nicht richtig gesetzt.");
            
            Object[] members = (Object[]) Array.newInstance(User.toClass(), 1);
            members[0] = owner;
            assertArrayEquals(members, gMembers.getOf(group), "Die Mitglieder wurden nicht richtig initialisiert.");
            
            if (reset) {
                groupResults[0] = false;
            }
        }

        @Order(1)
        @DisplayName("Group - Name")
        @ParameterizedTest(name = "\t- {displayName} {index}: {0}")
        @MethodSource
        @Hidden
        public void groupName(String name) {
            
            boolean reset = !groupResults[1];
            groupResults[1] = true;
            
            String testDesc = Integer.toString(random.nextInt());
            Object picture = newPicture.newInstance("", new int[1][1]);
            Object owner = newUser.newInstance(Integer.toString(random.nextInt()), Integer.toString(random.nextInt()), picture);
            Object group = newGroup.newInstance( "", testDesc, owner, picture);
            Object[] members = gMembers.getOf(group);
            
            gSetName.invokeOn(group, name);
            
            assertEquals(name, gName.getOf(group), "Der Setter setzt den Namen nicht richtig.");
            assertEquals(name, gGetName.invokeOn(group), "Der Getter gibt nicht den richtigen Wert zurück.");
            
            assertEquals(testDesc, gDescription.getOf(group), "Die BEschreibung sollte sich nicht verändern.");
            assertSame(owner, gOwner.getOf(group), "Der Besitzer sollte sich nicht verändern.");
            assertSame(members, gMembers.getOf(group), "Die Mitglieder sollten sich nicht verändern.");
            assertSame(picture, gPicture.getOf(group), "Das Bild sollte sich nicht verändern.");
            
            if (reset) {
                groupResults[1] = false;
            }
        }

        @Order(1)
        @DisplayName("Group - Description")
        @ParameterizedTest(name = "\t- {displayName} {index}: {0}")
        @MethodSource
        @Hidden
        public void groupDescription(String description) {
            
            boolean reset = !groupResults[2];
            groupResults[2] = true;
            
            String testName = Integer.toString(random.nextInt());
            Object picture = newPicture.newInstance("", new int[1][1]);
            Object owner = newUser.newInstance(Integer.toString(random.nextInt()), Integer.toString(random.nextInt()), picture);
            Object group = newGroup.newInstance(testName, "", owner, picture);
            Object[] members = gMembers.getOf(group);
            
            gSetDescription.invokeOn(group, description);
            
            assertEquals(description, gDescription.getOf(group), "Der Setter setzt die Beschreibung nicht richtig.");
            assertEquals(description, gGetDescription.invokeOn(group), "Der Getter gibt den falschen Wert zurück.");
            
            assertEquals(testName, gName.getOf(group), "Der Name solte sich nicht verändern.");
            assertSame(owner, gOwner.getOf(group), "Der Besitzer ollte sich nicht verändern.");
            assertSame(members, gMembers.getOf(group), "Die Mitglieder sollten sich nicht verändern.");
            assertSame(picture, gPicture.getOf(group), "Das Bild sollte sich nicht verändern.");
            
            if (reset) {
                groupResults[2] = false;
            }
        }
        
        @DisplayName("Group - Owner")
        @ParameterizedTest(name = "\t- {displayName} {index}: {0}")
        @MethodSource
        @Hidden
        public void groupOwner(String name, String description) throws IllegalArgumentException, IllegalAccessException {
            
            boolean reset = !groupResults[3];
            groupResults[3] = true;
            
            String testName = Integer.toString(random.nextInt());
            String testDesc = Integer.toString(random.nextInt());
            Object picture = newPicture.newInstance("", new int[1][1]);
            Object owner = newUser.newInstance(name, description, picture);
            Object sampleOwner = newUser.newInstance("","", newPicture.newInstance("", new int[1][1]));
            Object group = newGroup.newInstance(testName, testDesc, sampleOwner, picture);
            Object[] members = gMembers.getOf(group);
            
            gSetOwner.invokeOn(group, owner);
            assertSame(sampleOwner, gOwner.getOf(group), "Der owner sollte nicht gesetzt werden, wenn er kein mitglied ist.");
            
            members = (Object[]) Array.newInstance(User.toClass(), 1);
            members[0] = owner;
            gMembers.toField().set(group, members);
            
            gSetOwner.invokeOn(group, owner);
            
            assertSame(owner, gOwner.getOf(group), "Der setter setzt den Besitzer nicht richtig.");
            assertSame(owner, gGetOwner.invokeOn(group), "Der Getter gibt nicht den richtigen Wert zurück.");
            
            assertEquals(testName, gName.getOf(group), "Der Name sollte sich nicht ändern.");
            assertEquals(testDesc, gDescription.getOf(group), "Die beschreibung sollte sich nicht ändern.");
            assertSame(members, gMembers.getOf(group), "Die Mitglieder sollten sich nicht ändern.");
            assertSame(picture, gPicture.getOf(group), "Das Bild sollte sich nicht verändern.");
            
            if (reset) {
                groupResults[3] = false;
            }
        }

        @Order(1)
        @DisplayName("Group - Owner")
        @ParameterizedTest(name = "\t- {displayName} {index}: {0}")
        @MethodSource
        @Hidden
        public void groupPicture(String location, int[][] data) {
            
            boolean reset = !groupResults[4];
            groupResults[4] = true;
            
            String testName = Integer.toString(random.nextInt());
            String testDesc = Integer.toString(random.nextInt());
            Object picture = newPicture.newInstance(location, data);
            Object owner = newUser.newInstance(Integer.toString(random.nextInt()), Integer.toString(random.nextInt()), picture);
            Object group = newGroup.newInstance(testName, testDesc, owner, newPicture.newInstance("", new int[1][1]));
            Object[] members = gMembers.getOf(group);
            
            gSetPicture.invokeOn(group, picture);
            
            assertSame(picture, gPicture.getOf(group), "Der Setter setzt das Bild nicht richtig.");
            assertSame(picture, gGetPicture.invokeOn(group), "Der Getter gibt nicht den richtigen Wert zurück.");
            
            assertEquals(testName, gName.getOf(group), "Der Name sollte sich nciht verändern.");
            assertEquals(testDesc, gDescription.getOf(group), "Die Beschreibung sollte sich nicht verändern.");
            assertSame(owner, gOwner.getOf(group), "Der Besitzer sollte sich nicht verändern.");
            assertSame(members, gMembers.getOf(group), "Die Mitglieder sollten sich nicht verändern.");
            
            if (reset) {
                groupResults[4] = false;
            }
        }

        @Order(1)
        @DisplayName("Group - Members")
        @ParameterizedTest(name = "\t- {displayName} {index}: {0}")
        @MethodSource
        @Hidden
        public void groupMembers(String name0, String name1) {
            
            boolean reset = !groupResults[5];
            groupResults[5] = true;
            
            String testName = Integer.toString(random.nextInt());
            String testDesc = Integer.toString(random.nextInt());
            Object picture = newPicture.newInstance(Integer.toString(random.nextInt()), new int[1][1]);
            Object owner = newUser.newInstance(name0, Integer.toString(random.nextInt()), picture);
            Object group = newGroup.newInstance(testName, testDesc, owner, newPicture.newInstance(Integer.toString(random.nextInt()), new int[1][1]));
            Object[] members = gMembers.getOf(group);
            
            Object user = newUser.newInstance(name0, Integer.toString(random.nextInt()), newPicture.newInstance(Integer.toString(random.nextInt()), new int[1][1]));
            
            addUser.invokeOn(group, user);
            

            members = gMembers.getOf(group);
            List<Object> cMembers = new ArrayList<>();
            cMembers.add(owner);
            cMembers.add(user);
            
            for (int i = 0; i < members.length; i++) {
                
                if (cMembers.size() == 0) {
                    fail("Es wurden mehr Mitglieder gespeichert als beigetreten sind.");
                }
                if(!cMembers.remove(members[i])) {
                    fail("Es wurde ein Nutzer gespeichert welcher nie beigetreten ist.");
                }
            }
            
            assertEquals(0, cMembers.size(), "Es wurden nciht alle Nutzer gespeichert.");
            
            members = gGetMembers.invokeOn(group);
            cMembers.add(owner);
            cMembers.add(user);
            
            for (int i = 0; i < members.length; i++) {
                
                if (cMembers.size() == 0) {
                    fail("Es wurden mehr Mitglieder gespeichert als beigetreten sind.");
                }
                if(!cMembers.remove(members[i])) {
                    fail("Es wurde ein Nutzer gespeichert welcher nie beigetreten ist.");
                }
            }
            
            assertEquals(0, cMembers.size(), "Es wurden nicht alle Nutzer gespeichert.");
            
            removeUser.invokeOn(group, owner);
            
            assertSame(user, gOwner.getOf(group), "Der owner wurde nicht richtig gesetzt");
            
            members = (Object[]) Array.newInstance(User.toClass(), 1);
            members[0] = user;
            
            assertArrayEquals(members, gMembers.getOf(group), "Der Nutzer wurder nicht richtig entfernt.");
            assertArrayEquals(members, gGetMembers.invokeOn(group), "Der Getter gibt nicht den richtigen Wert zurück.");

            assertEquals(testName, gName.getOf(group), "Der Name sollte sich nciht verändern.");
            assertEquals(testDesc, gDescription.getOf(group), "Die beschreibung sollte sich nicht verändern.");
            
            if (reset) {
                groupResults[5] = false;
            }
        }
        
        @Order(1)
        @PublicTest
        @DisplayName("Group Public Test")
        public void groupPublic() {

            try {
                GroupTests.groupConstructor().forEach( args -> {

                    Object[] params = args.get();
                    
                    for (int i = 0; i < params.length; i++) {
                        if (params[i] instanceof Named) {
                            params[i] = ((Named<?>)params[i]).getPayload();
                        }
                    }
                    
                    groupConstructor((String)params[0], (String)params[1], (Object[])params[2]);
                });
            } catch (Exception e) {
                fail("Der Group Constructor funktioniert nicht wie erwartet.");
            }
        }
        
        
    }


    // =============================================================================== //
    // -------------------------------- Picture Tests -------------------------------- //
    // =============================================================================== //


    @Nested
    @TestClassAnnotation
    class PictureTests {
        
        private static Stream<Arguments> pictureConstructor() {
            return Stream.of(Arguments.of("Sample Location", 0, 0), Arguments.of(Named.of("<Random Location>", Integer.toString(random.nextInt())), Named.of("<Random height>", random.nextInt(1,100)), Named.of("<Random width>", random.nextInt(1100))));
        }
        private static Stream<Arguments> pictureThumbnails() {
            return Stream.of(Arguments.of("Sample Location"), Arguments.of(Named.of("<Random Location>", Integer.toString(random.nextInt()))));
        }
        
        private static String[] testNames = {"Constructor Test", "Thumbnail Test"};
        private static boolean[] pictureResults = new boolean[testNames.length];

        @Order(1)
        @DisplayName("Picture - Constructor")
        @Hidden
        @ParameterizedTest(name = "\t- {displayName} {index}: {0}, {1}, {2}")
        @MethodSource
        public void pictureConstructor(String location, int height, int width) {
            
            boolean reset = !pictureResults[0];
            pictureResults[0] = true;

            int[][] data = new int[height][width];
            Object picture = newPicture.newInstance(location, data);
            
            assertEquals(location, piLocation.getOf(picture), "Die Location wird nicht richtig gesetzt.");
            assertSame(data, piData.getOf(picture), "Das data Array wird nicht richtig gesetzt.");
            assertEquals(height, piHeight.getOf(picture),"Die Höhe wird nicht richtig berechnet.");
            assertEquals(width, piWidth.getOf(picture), "Die Breite wird nicht richtig berechnet.");
            assertArrayEquals((Object[])Array.newInstance(Picture.toClass(), 0), piThumbnails.getOf(picture));
            
            assertEquals(location, piGetLocation.invokeOn(picture), "Der Getter gibt nicht die richtige Location zurück.");
            assertSame(data, piGetData.invokeOn(picture), "Der Getter gibt nicht die richtigen Daten zurück.");
            assertEquals(height, piGetHeight.invokeOn(picture), "Der Getter gibt nicht die richtige Höhe zurück.");
            assertEquals(width, piGetWidth.invokeOn(picture), "Der Getter gibt nicht die richtige Breite zurück.");
            assertArrayEquals((Object[])Array.newInstance(Picture.toClass(), 0), piGetThumbnails.invokeOn(picture), "Der Getter gibt nicht die richtigen thumbnails zurück.");
            
            if (reset) {

                pictureResults[0] = false;
            }
        }

        @Order(1)
        @DisplayName("Picture - Public")
        @PublicTest
        public void picturePublic() {
        	
        	try {
                PictureTests.pictureConstructor().forEach( args -> {

                    Object[] params = args.get();
                    
                    for (int i = 0; i < params.length; i++) {
                        if (params[i] instanceof Named) {
                            params[i] = ((Named<?>)params[i]).getPayload();
                        }
                    }
                    
                    pictureConstructor((String)params[0], (int)params[1], (int)params[2]);
                });
            } catch (Exception e) {
                fail("Der Picture Constructor funktioniert nicht wie erwartet.");
            }
        }
        
        @Order(1)
        @DisplayName("Picture - Thumbnails")
        @ParameterizedTest(name = "\t- {displayName} {index}: {0}")
        @MethodSource
        @Hidden
        public void pictureThumbnails(String location) {
            
            boolean reset = !pictureResults[1];
            pictureResults[1] = true;

            int[][] data0 = new int[random.nextInt(100)][random.nextInt(100)];
            int[][] data1 = new int[random.nextInt(100)][random.nextInt(100)];
            String testLocation = Integer.toString(random.nextInt());
            Object picture = newPicture.newInstance(testLocation, data0);
            Object thumbnail = newPicture.newInstance(location, data1);
            
            Object thumbnails = Array.newInstance(Picture.toClass(), 1);
            Array.set(thumbnails, 0, thumbnail);
            
            piSetThumbnails.invokeOn(picture, thumbnails);

            assertSame(thumbnails, piThumbnails.getOf(picture), "Der Setter setzt die Thumbnails nicht richtig.");
            assertSame(thumbnails, piGetThumbnails.invokeOn(picture), "Der Getter gibt nicht den richtigen Wert zurück.");
            
            assertEquals(testLocation, piLocation.getOf(picture), "Die Location soll nicht geändert werden.");
            assertSame(data0, piData.getOf(picture), "Die bilddaten sollen nicht verändert werden.");
            
            if (reset) {

                pictureResults[1] = false;
            }
        }
        
        @Order(2)
        @HiddenTest
        @DisplayName("Grading - Picture")
        public void gradingPicture() {
            
            boolean allGood = true;
            
            StringBuilder result = new StringBuilder("Folgende Tests für Bilder sind fehlgeschlagen:\n");
            
            for (int i = 0; i < pictureResults.length; i++) {
                if (pictureResults[i]) {
                    allGood = false;
                    result.append(testNames[i] + "\n");
                }
            }
            
            if (!allGood) {
                fail(result.toString() + "\n0/1P: Der Punkt auf den Inhalt der Klasse 'Picture' wurde nicht vergeben. Um zu verstehen, warum, sieh dir die Ergebnisse der darauf bezogenen Tests an.");
            }

        }
        
    }


    // =================================================================================== //
    // -------------------------------- Interaction Tests -------------------------------- //
    // =================================================================================== //


    @Nested
    @Hidden
    @TestClassAnnotation
    class InteractionTests {

        

        static String[] testNames = {"Constructor Test"};
        static boolean[] interactionResults = new boolean[testNames.length];

        static Stream<Arguments> interactionConstructor() {
            return Stream.of(Arguments.of(1, "Sample Name"), Arguments.of(random.nextInt(), Named.of("<Random User Name>", Integer.toString(random.nextInt()))));
        }

        @Order(1)
        @DisplayName("Interaction - Constructor")
        @ParameterizedTest(name = "\t- {displayName} {index}: {0}, {1}")
        @MethodSource
        public void interactionConstructor(int type, String name) {
            
            boolean reset = !interactionResults[0];
            interactionResults[0] = true;
            
            Object picture = newPicture.newInstance("", new int[1][1]);
            Object user = newUser.newInstance(name, "", picture);
            Object interaction = newInteraction.newInstance(user, type);
            
            assertSame(user, iUser.getOf(interaction), "Der Nutzer wird nicht richtig gesetzt");
            assertSame(user, iGetUser.invokeOn(interaction), "Der Getter gibt nicht den richtigen Nutzer zurück.");
            assertEquals(type, iType.getOf(interaction), "Der Typ wird nicht richtig gesetzt.");
            assertEquals(type, iGetType.invokeOn(interaction), "Der Getter gibt nicht den richtigen Typ zurück.");
            
            if (reset) {

                interactionResults[0] = false;
            }
        }
        
        @Order(2)
        @HiddenTest
        @DisplayName("Grading - Interaction")
        public void gradingInteraction() {
            
            boolean allGood = true;
            
            StringBuilder result = new StringBuilder("Folgende Tests für Interactions sind fehlgeschlagen:\n");
            
            for (int i = 0; i < interactionResults.length; i++) {
                if (interactionResults[i]) {
                    allGood = false;
                    result.append(testNames[i] + "\n");
                }
            }
            
            if (!allGood) {
                fail(result.toString() + "\n0/1P: Der Punkt auf den Inhalt der Klasse 'Interaction' wurde nicht vergeben. Um zu verstehen, warum, sieh dir die Ergebnisse der darauf bezogenen Tests an.");
            }

        }
   
    }
    
}
