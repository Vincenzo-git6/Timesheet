package com.axcent.TimeSheet.repositories.customs;

import java.util.Map;

public interface CustomUtenteRepository {
     Map<String, Object> getUtenteInfo(Long id);

     String getUtenteUsername(Long id);

     Long findUserIdByUsername(String username);

    String getEmailByUserId(Long userId);
}
