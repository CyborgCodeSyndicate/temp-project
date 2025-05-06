package com.theairebellion.zeus.framework.hooks;

/**
 * Specifies the timing for hook execution relative to the test lifecycle.
 *
 * <p>Use {@link #BEFORE} to execute a hook before all tests in a class are run,
 * and {@link #AFTER} to execute a hook after all tests in a class have completed.</p>
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public enum HookExecution {

   /**
    * Indicates that the hook should run before tests (setup phase).
    */
   BEFORE,

   /**
    * Indicates that the hook should run after tests (teardown phase).
    */
   AFTER
}
