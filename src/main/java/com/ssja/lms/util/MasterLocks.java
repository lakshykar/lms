package com.ssja.lms.util;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantLock;

public class MasterLocks {

    public static ConcurrentMap<String, ReentrantLock> bookLock = new ConcurrentHashMap<>();
    public static ConcurrentMap<String, ReentrantLock> issuedBookLock = new ConcurrentHashMap<>();


	private MasterLocks() {
		super();
	}
	
}
