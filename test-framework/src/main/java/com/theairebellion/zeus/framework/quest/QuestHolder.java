package com.theairebellion.zeus.framework.quest;

public class QuestHolder {

    private static final ThreadLocal<SuperQuest> THREAD_LOCAL_QUEST = new ThreadLocal<>();


    private QuestHolder() {
    }


    public static void set(SuperQuest quest) {
        THREAD_LOCAL_QUEST.set(quest);
    }


    public static SuperQuest get() {
        return THREAD_LOCAL_QUEST.get();
    }


    public static void clear() {
        THREAD_LOCAL_QUEST.remove();
    }

}
