package com.theairebellion.zeus.framework.quest;

public class QuestHolder {

    private static final ThreadLocal<Quest> threadLocalQuest = new ThreadLocal<>();


    public static void set(Quest quest) {
        threadLocalQuest.set(quest);
    }


    public static Quest get() {
        return threadLocalQuest.get();
    }


    public static void clear() {
        threadLocalQuest.remove();
    }

}
