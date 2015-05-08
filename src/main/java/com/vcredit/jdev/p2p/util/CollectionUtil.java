package com.vcredit.jdev.p2p.util;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class CollectionUtil {
	/**
	 * @param <T>
	 * @param set
	 * @return list
	 */
	public static <T> List<T> set2List(Set<T> set) {
		if (set == null) {
			return null;
		}

		List<T> list = new ArrayList<T>();

		Iterator<T> iterator = set.iterator();
		while (iterator.hasNext()) {
			list.add(iterator.next());
		}

		return list;
	}

	/**
	 * @param <T>
	 * @param list
	 * @return list
	 */
	public static <T> Set<T> list2Set(List<T> list) {
		Set<T> set = new HashSet<T>();
		for (T t : list) {
			set.add(t);
		}
		return set;
	}

	/**
	 * @param <T>
	 * @param ary
	 * @return 将数组转为List
	 */
	public static <T> List<T> ary2List(T[] ary) {
		List<T> list = new ArrayList<T>();
		for (int i = 0; i < ary.length; i++) {
			list.add(ary[i]);
		}
		return list;
	}

	public static Long[][] list2ary(List<Long[]> timePartList) {
		Long[][] ary = new Long[timePartList.size()][];
		for (int i = 0; i < timePartList.size(); i++) {
			ary[i] = timePartList.get(i);
		}
		return ary;
	}

	/**
	 * @param higherTimePart
	 * @param lowerTimePart
	 * @return
	 */
	public static boolean contains(List<Long[]> higherPart, List<Long[]> lowerPart) {
		if (lowerPart.size() == 0 && higherPart.size() != 0)
			return true;

		for (int i = 0; i < lowerPart.size(); i++) {
			Long[] lPart = lowerPart.get(i);
			int j = 0;
			for (j = 0; j < higherPart.size(); j++) {
				Long[] hPart = higherPart.get(j);
				if (hPart[0] <= lPart[0] && hPart[1] >= lPart[1]) {
					// higherTimePart第i个段冲突
					break;
				}
			}

			if (j == higherPart.size()) {
				// 不冲突
				return false;
			}

		}
		return true;
	}
	
	/**
	 * 检查集合是否有重复的部分
	 * @param higherPart 有序段
	 * @return
	 */
	public static boolean checkOverlapping(List<Long[]> higherPart) {
		
		Long[][] parts = higherPart.toArray(new Long[][]{});
		
		//先排序(从小到大)
		for(int i=0;i<parts.length;i++) {
			for(int j=i;j<parts.length;j++) {
				if(parts[i][0] > parts[j][0]) {
					Long[] temp = parts[i];
					parts[i] = parts[j];
					parts[j] = temp;
				}
			}
		}
		
		for(int i=0;i<parts.length - 1;i++) {
			if(parts[i][1] >= parts[i+1][0]) {
				return true;
			}
		}
		
		
		return false;
	}

	/**
	 * 假如有三个片段[1,5],[2,4],[3,7]，返回[1,7]
	 * 
	 * @param partList
	 * @return 把能合并的片段合并
	 */
	public static List<Long[]> dealWidthTimePart(List<Long[]> partList) {
		Long[][] newAry = CollectionUtil.list2ary(partList);
		for (int i = 0; i < newAry.length; i++) {
			for (int j = 0; j < newAry.length - i - 1; i++) {
				if (newAry[j][0] > newAry[j + 1][0]) {
					Long[] temp = newAry[j];
					newAry[j] = newAry[j + 1];
					newAry[j + 1] = temp;
				}
			}
		}
		return mergePart(newAry);
	}

	private static List<Long[]> mergePart(Long[][] sortAry) {
		List<Long[]> resultAry = new ArrayList<Long[]>();
		for (int i = 0; i < sortAry.length; i++) {
			if (i < sortAry.length - 1) {
				if (sortAry[i][1] >= sortAry[i + 1][0] && sortAry[i][1] >= sortAry[i + 1][1]) {
					sortAry[i + 1] = sortAry[i];
					sortAry[i] = null;
				} else if (sortAry[i][1] >= sortAry[i + 1][0] && sortAry[i][1] < sortAry[i + 1][1]) {
					sortAry[i + 1][0] = sortAry[i][0];
					sortAry[i] = null;
				} else {
					resultAry.add(sortAry[i]);
				}
			} else {
				resultAry.add(sortAry[sortAry.length - 1]);
			}
		}

		return resultAry;
	}

	/**
	 * @param higherHostNameList
	 * @param lowerHostNameList
	 * @return
	 */
	public static boolean containsStr(List<String> higherHostNameList, List<String> lowerHostNameList) {
		for (int i = 0; i < lowerHostNameList.size(); i++) {
			if (higherHostNameList.contains(lowerHostNameList.get(i))) {
				continue;
			}

			return false;
		}

		return true;
	}

	/**
	 * 排序
	 * 
	 * @param srcList
	 */
	public static List<Integer> sort(List<Integer> srcList) {
		Object[] ary = srcList.toArray();
		java.util.Arrays.sort(ary);

		List<Integer> resultList = new ArrayList<Integer>();
		for (Object val : ary) {
			resultList.add(Integer.parseInt(val.toString()));
		}

		return resultList;
	}

	/**
	 * 根据排好序的sortedAllList返回moveList的顺序列表。moveList是sortedAllList的子集
	 * 
	 * @param moveList
	 * @param sortedAllList
	 * @return
	 */
	public static List<Integer> sortAgaistSortedList(List<Integer> moveList, List<Integer> sortedAllList) {
		List<Integer> tempList = new ArrayList<Integer>();
		for (Integer sortedVal : sortedAllList) {
			if (moveList.contains(sortedVal)) {
				tempList.add(sortedVal);
			}
		}

		return tempList;
	}

	public static void copy(List<Integer> destList, List<Integer> srcList) {
		destList.removeAll(destList);
		for (Integer val : srcList) {
			destList.add(val);
		}
	}

	/**
	 * 将double型的整形数据列表转化为long型列表
	 * 
	 * @param doubleIdList
	 * @return
	 */
	public static List<Long> transferToLongList(List doubleIdList) {
		List<Long> longIdList = new ArrayList<Long>();

		for (Object obj : doubleIdList) {
			if (obj instanceof Double) {
				try {
					Long tempId = ((Double) obj).longValue();
					longIdList.add(tempId);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (obj instanceof Long) {
				longIdList.add((Long) obj);
			} else if (obj instanceof Integer) {
				longIdList.add(Long.parseLong(obj.toString()));
			}
		}

		return longIdList;
	}

	public static List<Integer> transferToIntegerList(List doubleIdList) {
		List<Integer> intIdList = new ArrayList<Integer>();

		for (Object obj : doubleIdList) {
			if (obj instanceof Double) {
				try {
					//Cannot cast from Double to Integer
					Integer tempId = ((Double) obj).intValue();
					intIdList.add(tempId);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (obj instanceof Integer) {
				intIdList.add((Integer) obj);
			} else if (obj instanceof Integer) {
				intIdList.add(Integer.parseInt(obj.toString()));
			}
		}

		return intIdList;
	}

	/**
	 * 将两个list中值相同数据删除，剩下不相同的, 返回相同的
	 * 
	 * @param list1
	 * @param list2
	 */
	public static List<Integer> checkIdentical(List<Integer> list1, List<Integer> list2) {
		List<Integer> resultList = new ArrayList<Integer>();

		Iterator<Integer> iterator1 = list1.iterator();
		while (iterator1.hasNext()) {
			Integer next1 = iterator1.next();
			Iterator<Integer> iterator2 = list2.iterator();
			while (iterator2.hasNext()) {
				Integer next2 = iterator2.next();
				if (next1.intValue() == next2.intValue()) {
					iterator1.remove();
					iterator2.remove();
					resultList.add(next1.intValue());
					break;
				}
			}
		}

		return resultList;
	}

	/**
	 * added by liyong
	 */
	public static boolean isEmpty(Collection c) {
		return c == null || c.isEmpty();
	}

	/**
	 * added by liyong
	 */
	public static boolean isEmpty(Map map) {
		return map == null || map.isEmpty();
	}

	/**
	 * added by liyong
	 */
	public static boolean isNotEmpty(Collection c) {
		return !isEmpty(c);
	}

	/**
	 * added by liyong
	 */
	public static <T> Collection<T> subtract(Collection<T> c1, Collection<T> c2) {
		if (isEmpty(c1) )
			return new ArrayList<T>(0);
		if (isEmpty(c2))
			return new ArrayList<T>(c1);

		List<T> result = new ArrayList<T>(c1);
		for (Iterator<T> it = c2.iterator(); it.hasNext();) {
			result.remove(it.next());
		}
		return result;
	}

	/**
	 * added by liyong
	 */
	public static <T> Collection<T> same(Collection<T> c1, Collection<T> c2) {
		if (isEmpty(c1) || isEmpty(c2))
			return new ArrayList<T>(0);

		List<T> result = new ArrayList<T>();
		for (T item : c2) {
			if (c1.contains(item)) {
				result.add(item);
			}
		}
		return result;
	}

	/**
	 * added by liyong
	 */

	public static <T> Collection<T>[] analyse(Collection<T> oldC, Collection<T> newC) {
		return new Collection[] { subtract(newC, oldC), subtract(oldC, newC), same(oldC, newC) };
	}

	public static List<String>[] diff(List<String> list1, List<String> list2) {
		if (list1 == null)
			list1 = new ArrayList<String>();
		if (list2 == null)
			list2 = new ArrayList<String>();

		List<String> toAddList = new ArrayList<String>();
		List<String> toDelList = new ArrayList<String>();

		for (String str1 : list1) {
			if (!list2.contains(str1)) {
				toAddList.add(str1);
			}
		}

		for (String str2 : list2) {
			if (!list1.contains(str2)) {
				toDelList.add(str2);
			}
		}

		return new List[] { toAddList, toDelList };
	}

	public static void deleteDuplicate(List<Integer> list) {
		List<Integer> tempList = new ArrayList<Integer>();
		
		Iterator<Integer> iter = list.iterator();
		while(iter.hasNext()) {
			Integer current = iter.next();
			if(tempList.contains(current)) {
				iter.remove();
			}else {
				tempList.add(current);
			}
		}
	}
	
	public static List<Long[]> minus(List<Long[]> srcList, List<Long[]> minusList) {
		srcList = sortByFirst(srcList);
		minusList = sortByFirst(minusList);
		
		minusList = dealWidthTimePart(minusList);
		List<Long[]> resultList = new ArrayList<Long[]>();
		
		int i = 0;
		int j = 0;
		while(true) {
			
			if(i >= srcList.size()) break;
			if(j >= minusList.size()) break;
			
			Long[] mainpart = srcList.get(i);
			Long[] part = minusList.get(j);
			
			if(part[0] > mainpart[1]){
				resultList.add(mainpart);
				i++;
				continue;
			}
			
			if(part[1] < mainpart[0]) {
				j++;
				continue;
			}
			
			if(part[0] <= mainpart[0] && part[1] >= mainpart[1]) {
				//循环回来再减一遍
				part[0] = mainpart[1];
				i++;
				continue;
			}
			
			if(part[0] >= mainpart[0] && part[1] <= mainpart[1]) {
				resultList.add(new Long[]{mainpart[0], part[0]});
				mainpart[0] = part[1];
				j++;
				continue;
			}
			
			if(part[0] <= mainpart[0]) {
				mainpart[0] = part[1];
				i++;
				resultList.add(new Long[]{part[1], mainpart[1]});
			}else {
				resultList.add(new Long[]{mainpart[0], part[0]});
				part[0] = mainpart[1];
				i++;
			}
		}
		
		if(j >= minusList.size() && i < srcList.size()) {
			resultList.addAll(srcList.subList(i, srcList.size()));
		}
		
		return resultList;
	}

	/**
	 * @param srcList
	 * @return
	 */
	private static List<Long[]> sortByFirst(List<Long[]> srcList) {
		Long[][] parts = srcList.toArray(new Long[][]{});
		//先排序(从小到大)
		for(int i=0;i<parts.length;i++) {
			for(int j=i;j<parts.length;j++) {
				if(parts[i][0] > parts[j][0]) {
					Long[] temp = parts[i];
					parts[i] = parts[j];
					parts[j] = temp;
				}
			}
		}
		
		List<Long[]> resultList = new ArrayList<Long[]>();
		for(Long[] part : parts) {
			resultList.add(part);
		}
		return resultList;
	}
}
