package rest.mybatis.dao.eBillDao;

import java.util.List;
import java.util.Map;

public interface EleBillMapper {

	<T> List<T> queryListByPage(Map paramMap);
	
	 int insertBatch(List list);
	 
	 int updateBatch(List list);
	 
	 int deleteBatch(List list);
}
