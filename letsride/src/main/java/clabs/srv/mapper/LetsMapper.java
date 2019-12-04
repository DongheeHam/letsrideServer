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

	List<Map<String, Object>> getAllRoomByAct();

	Map<String, Object> getMyRoom(Map<String, String> params);

	Map<String, Object> getAttendedRoom(Map<String, String> params);

	int makeMyRoom(Map<String, String> params);

	int attendRoom(Map<String, String> params);

	Map<String, Object> getRoom(Map<String, String> params);

	int deleteMyRoom(Map<String, String> params);

	int goOutRoom(Map<String, String> m);

	List<Map<String, Object>> getUserInRoom(Map<String, String> params);

	List<Map<String, Object>> getUserInfoThisRoom(Map<String, String> params);

	void unlink(Map<String, String> params);

	


}
