package gr.uoa.di.madgik.utils;

import java.util.UUID;

@FunctionalInterface
public interface UUIDGenerator {

    UUID generate();

}
