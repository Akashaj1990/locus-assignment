package com.locus.auth.processor;

import com.locus.auth.dao.InMemoryDb;
import com.locus.auth.model.Resource;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class AsynchTaskProcessor {

    private static final InMemoryDb database = InMemoryDb.getInstance();
    private static final ExecutorService executor = Executors.newFixedThreadPool(50);

    public static void submitTask(Runnable worker) {
        executor.submit(worker);
    }

    public static void shutdown() {
        try {
            executor.shutdown();
            executor.awaitTermination(100, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Data
    @Builder
    @ToString
    public static class SaveWorker implements Runnable {

        private Resource<?> resource;
        private Runnable callback;

        @Override
        public void run() {
            database.saveOrUpdateResource(resource);
            if (null != callback) {
                callback.run();
            }
        }
    }

    @Data
    @Builder
    @ToString
    public static class DeleteWorker implements Runnable {

        private Long resourceId;
        private Runnable callback;

        @Override
        public void run() {
            database.deleteResource(resourceId);
            if (null != callback) {
                callback.run();
            }
        }
    }
}
