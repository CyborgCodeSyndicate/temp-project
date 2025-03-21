package com.theairebellion.zeus.framework.quest;

/**
 * Provides thread-local storage for the current test execution context.
 * <p>
 * This class holds a {@code SuperQuest} instance for the current thread,
 * allowing test components to access the active test context during execution.
 * It ensures that each thread's execution context remains isolated.
 * </p>
 *
 * <p>
 * Use {@code set(...)} to assign the current test context, {@code get()} to retrieve it,
 * and {@code clear()} to remove it when the test execution is complete.
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
public class QuestHolder {

    private static final ThreadLocal<SuperQuest> THREAD_LOCAL_QUEST = new ThreadLocal<>();


    private QuestHolder() {
    }

    /**
     * Sets the current test execution context.
     *
     * @param quest the {@code SuperQuest} instance representing the current test context.
     */
    public static void set(SuperQuest quest) {
        THREAD_LOCAL_QUEST.set(quest);
    }

    /**
     * Retrieves the current test execution context.
     *
     * @return the {@code SuperQuest} instance associated with the current thread, or {@code null} if not set.
     */
    public static SuperQuest get() {
        return THREAD_LOCAL_QUEST.get();
    }

    /**
     * Clears the current test execution context from thread-local storage.
     */
    public static void clear() {
        THREAD_LOCAL_QUEST.remove();
    }

}
