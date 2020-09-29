package com.ciecc.common.identity;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/*
 * Name      : SnowFlakeIDCreator
 * Creator   : louis zhang
 * Function  : 雪花算法ID生成器实现
 * Date      : 2016-1-18
 */
public class SnowFlakeIDCreator implements IDCreator<Long>{

    private static final long MAX_TIMESTAMP = -1L^(-1L << 41);

    private static final long MAX_WORK_ID =  -1L^(-1L << 5);

    private static final long MAX_CENTER_ID =  -1L^(-1L << 5);

    private static final long MAX_SEQUENCE_ID =  -1L^(-1L << 12);

    private ReadWriteLock rwLock = new ReentrantReadWriteLock(true);

    private long workId;

    private long centerId;

    private  long baseTimeStamp;

    private volatile long beforeTimeStamp;

    private AtomicLong sequence;


    public SnowFlakeIDCreator(long workId, long centerId, long baseTimeStamp) {
        if(workId > MAX_WORK_ID || workId < 0) {
            throw new IllegalArgumentException("illegally workid");
        }
        if(centerId > MAX_CENTER_ID || centerId < 0) {
            throw new IllegalArgumentException("illegally centerId");
        }
        if(baseTimeStamp > System.currentTimeMillis() || baseTimeStamp < 0){
            throw new IllegalArgumentException("illegally baseTimeStamp");
        }
        this.workId = workId;
        this.centerId = centerId;
        this.baseTimeStamp = baseTimeStamp;
        this.sequence = new AtomicLong();
    }

    @Override
    public Long create() {
        long result = 0L;
        long seq = 0L;
        long current = System.currentTimeMillis();
        long time = current - this.baseTimeStamp;
        if(time > MAX_TIMESTAMP){
            return -1L;
        }
        rwLock.readLock().lock();
        long before = this.beforeTimeStamp;
        rwLock.readLock().unlock();
        if(before == current) {
            seq = this.sequence.getAndIncrement();
        } else {
            rwLock.writeLock().lock();
            if(this.beforeTimeStamp != current) {
                this.beforeTimeStamp =  current;
                this.sequence.set(1);
                rwLock.writeLock().unlock();
            } else {
                rwLock.readLock().lock();
                rwLock.writeLock().unlock();
                seq = this.sequence.getAndIncrement();
                rwLock.readLock().unlock();
            }
        }
        if (seq >= MAX_SEQUENCE_ID){
            result = -1L;
        } else {
            result |= time;
            result <<= 5;
            result |= this.centerId;
            result <<= 5;
            result |= this.workId;
            result <<= 12;
            result |= (seq + 1);
        }

        return result;
    }
}
