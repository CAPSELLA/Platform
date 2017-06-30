package gr.uoa.di.madgik.repo;

import java.util.List;

import gr.uoa.di.madgik.model.Group;


public interface GroupRepoExtension {
    List<String> getAllGroupNames();
    void create(Group group);
}
