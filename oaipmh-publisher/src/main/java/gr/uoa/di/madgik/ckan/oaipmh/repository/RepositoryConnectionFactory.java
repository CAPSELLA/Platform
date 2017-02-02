package gr.uoa.di.madgik.ckan.oaipmh.repository;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author  
 *
 */
public class RepositoryConnectionFactory {

    private static Repository repository;

    private RepositoryConnectionFactory() {
    }

    /**
     *
     * @return the registered {@link Repository} of the
     * {@link RepositoryConnectionFactory}
     * @throws RepositoryRegistrationException if no {@link Repository} is
     * registered into {@link RepositoryConnectionFactory}
     */
    public static Repository getRepository() throws RepositoryRegistrationException {
        Logger.getLogger(RepositoryConnectionFactory.class.getName()).log(Level.DEBUG, "getRepository");
        if (repository == null) {
            Logger.getLogger(RepositoryConnectionFactory.class.getName()).log(Level.INFO, "repository is not registered to RepositoryConnectionFactory,"
                    + " call RepositoryConnectionFactory.registerRepository");

            throw new RepositoryRegistrationException(
                    "repository is not registered to RepositoryConnectionFactory,"
                    + " call RepositoryConnectionFactory.registerRepository");
        }
        return repository;
    }

    /**
     *
     * @param repository
     * @throws RepositoryRegistrationException if a {@link Repository} is
     * already registered into {@link RepositoryConnectionFactory}
     */
    public static void registerRepository(Repository repository) throws RepositoryRegistrationException {
        Logger.getLogger(RepositoryConnectionFactory.class.getName()).log(Level.DEBUG, "registerRepository");

        if (RepositoryConnectionFactory.repository == null) {
            RepositoryConnectionFactory.repository = repository;
        } else {
            Logger.getLogger(RepositoryConnectionFactory.class.getName()).log(Level.INFO, "Repository is already registered");
            throw new RepositoryRegistrationException("Repository is already registered");
        }
    }
}
