package com.axcent.TimeSheet.services;

import com.axcent.TimeSheet.repositories.customs.CustomUtenteRepository;
import jakarta.persistence.EntityManager;
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
}
