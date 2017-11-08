package rest.mybatis.dao.eBillDao;

import java.util.List;
import java.util.Map;

public interface EleBillMapper {

	<T> List<T> queryListByPage(Map paramMap);
}
