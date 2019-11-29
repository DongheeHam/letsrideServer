package clabs.srv.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface LetsMapper {

	Map<String, Object> getUserById(Map<String, String> params);

	int updateUser(Map<String, String> params);

	int insertUser(Map<String, String> params);

	Map<String, Object> getUserByLT(Map<String, String> params);
	Map<String, Object> getUserByLT(String l_token);
	
	int updateLtoken(Map<String, String> params);

	int openSession(Map<String, Object> map);

	


}
