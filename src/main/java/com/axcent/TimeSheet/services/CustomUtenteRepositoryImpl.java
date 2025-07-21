package com.axcent.TimeSheet.services;

import com.axcent.TimeSheet.repositories.customs.CustomUtenteRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class CustomUtenteRepositoryImpl implements CustomUtenteRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Map<String, Object> getUtenteInfo(Long id) {
        String sql = """
            SELECT
                        a.nome,
                        a.cognome,
                        a.sede
                    FROM
                        utente u
                    INNER JOIN
                        anagrafica_utente a ON a.utente_id = u.id
                    WHERE
                        u.id = :id;
        """;

        NativeQuery<?> query = (NativeQuery<?>) entityManager
                .createNativeQuery(sql)
                .unwrap(NativeQuery.class);

        query.setParameter("id", id);
        query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        return (Map<String, Object>) query.getSingleResult();
    }

    public String getUtenteUsername(Long id) {
        String sql = """
        SELECT u.username
        FROM utente u
        WHERE u.id = :id
    """;

        Object result = entityManager.createNativeQuery(sql)
                .setParameter("id", id)
                .getSingleResult();

        return result != null ? result.toString() : null;
    }

    @Override
    public Long findUserIdByUsername(String username) {
        String sql = """
        SELECT u.id
        FROM utente u
        WHERE u.username = :username
    """;

        Object result = entityManager.createNativeQuery(sql)
                .setParameter("username", username)
                .getSingleResult();

        return result != null ? (Long) result : null;
    }

    @Override
    public String getEmailByUserId(Long userId) {
        String sql = """
            SELECT u.email
            FROM utente u
            WHERE u.id = :userId
        """;

        try {
            Object result = entityManager.createNativeQuery(sql)
                    .setParameter("userId", userId)
                    .getSingleResult();


            return result != null ? result.toString() : null;
        } catch (NoResultException e) {
                        return null;
        }
    }


}
