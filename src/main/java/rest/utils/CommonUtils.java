package rest.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import rest.mybatis.dao.eBillDao.EleBillMapper;

public class CommonUtils {

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	/**
	 * TODO 日期类型转换为字符串
	 * @param d
	 * @return
	 */
	public static String dateToStr(Date d){
		
		if(null != d){
			String s = sdf.format(d);
			return s;
		}
		return "date to string failed";
	}
	/**
	 * TODO 字符串类型转换为日期
	 * @param sd
	 * @return
	 */
	public static Date strToDate(String sd){
		
		if(null != sd && !"".equals(sd)){
			Date d;
			try {
				d = sdf.parse(sd);
				return d;
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public static String charToString(String [] array){
		if(null != array){
			StringBuilder sb = new StringBuilder();
			for(int i=0; i< array.length; i++){
				sb.append(array[i]);
			}
			return sb.toString();
		}
		return null;
	}
	
	public <T> PageInfo queryListByPage(Map<String,T> map,EleBillMapper mapper) {
		Integer pageNo = (Integer)map.get("pageNo");
		pageNo = pageNo == null?1:pageNo;
		Integer pageSize = (Integer)map.get("pageSize");
		pageSize = pageSize == null?10:pageSize;
		PageHelper.startPage(pageNo, pageSize);
		
		
		List<T> list = mapper.queryListByPage(map);
		PageInfo pageInfo = new PageInfo(list);
		
		return pageInfo;
		
	}
	
}
