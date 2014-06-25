/**
 * name: 謝明翰
 * student number: F74001056
 * description: 挑出網頁中交易次數最多的地點，並列出最高成交價與最低成交價。
 */
import java.net.*;
import java.io.*;
import java.util.*;
import org.json.*;

public class TocHw4 {
	public static void main(String[] args) throws JSONException {
		ArrayList<String> time_list = new ArrayList<String>(0);
		ArrayList<String> road_list = new ArrayList<String>(0);
		ArrayList<Integer> highest_list = new ArrayList<Integer>(0);
		ArrayList<Integer> lowest_list = new ArrayList<Integer>(0);
		String address = "", year_month = "", max_road = "";
		int trade_volume = 1, max_trade_volume = 0;
		int price = 0, highest = 0, lowest = 0, max_highest = 0, max_lowest = 0;
		int address_length = 0, index = 0;
		
		try 
		{
			URL url = new URL(args[0]);
			URLConnection connect = url.openConnection();
			InputStreamReader stream = new InputStreamReader(connect.getInputStream(), "UTF-8");
			JSONArray json_data = new JSONArray(new JSONTokener(stream));

			for(int json_index = 0; json_index < json_data.length(); json_index++)
			{
				address = json_data.getJSONObject(json_index).getString("土地區段位置或建物區門牌");
				
				if(address.lastIndexOf("路") != -1)
				{
					address_length = address.lastIndexOf("路");
				}
				else if(address.lastIndexOf("街") != -1)
				{
					address_length = address.lastIndexOf("街");
				}
				else if(address.lastIndexOf("大道") != -1)
				{
					address_length = address.lastIndexOf("大道") + 1;
				}
				else if(address.lastIndexOf("巷") != -1)
				{
					address_length = address.lastIndexOf("巷");
				}
				else
				{
					continue;
				}

				address = address.substring(0, address_length + 1);
				year_month = Integer.toString(json_data.getJSONObject(json_index).getInt("交易年月"));
				price = json_data.getJSONObject(json_index).getInt("總價元");
				
				for(index = 0; index < road_list.size(); index++)
				{
					if(address.equals(road_list.get(index)))
					{
						break;
					}
				}
				if(index == road_list.size())
				{
					road_list.add(address);
					time_list.add(year_month);
					highest_list.add(price);
					lowest_list.add(price);
				}
				else
				{
					for(; index < road_list.size(); index++)
					{
						if(!address.equals(road_list.get(index)))
						{
							break;
						}
						
						if(year_month.equals(time_list.get(index)))
						{
							if(price > highest_list.get(index))
							{
								highest_list.set(index, price);
							}
							else if(price < lowest_list.get(index))
							{
								lowest_list.set(index, price);
							}
							break;
						}
					}
					if(index < road_list.size() && !address.equals(road_list.get(index)))
					{
						road_list.add(index, address);
						time_list.add(index, year_month);
						highest_list.add(index, price);
						lowest_list.add(index, price);
					}
					else if(index == road_list.size())
					{
						road_list.add(index, address);
						time_list.add(index, year_month);
						highest_list.add(index, price);
						lowest_list.add(index, price);
					}
				}
			}
			
			trade_volume = 1;
			for(index = 1; index < road_list.size(); index++)
			{
				if(road_list.get(index).equals(road_list.get(index - 1)))
				{
					trade_volume++;
				}
				else
				{
					if(trade_volume > max_trade_volume)
					{
						max_trade_volume = trade_volume;
						max_road = road_list.get(index - 1);
					}
					trade_volume = 1;
				}
			}
			if(trade_volume > max_trade_volume)
			{
				max_trade_volume = trade_volume;
				max_road = road_list.get(index - 1);
			}
			
			for(index = 0; !road_list.get(index).equals(max_road); index++);
			trade_volume = 1;
			highest = highest_list.get(index);
			lowest = lowest_list.get(index);
			for(index = index + 1; index < road_list.size(); index++)
			{
				if(road_list.get(index).equals(road_list.get(index - 1)))
				{
					trade_volume++;
					
					if(highest_list.get(index) > highest)
					{
						highest = highest_list.get(index);
					}
					if(lowest_list.get(index) < lowest)
					{
						lowest = lowest_list.get(index);
					}
				}
				else
				{
					if(trade_volume == max_trade_volume)
					{
						max_road = road_list.get(index - 1);
						
						max_highest = highest;
						max_lowest = lowest;
						
						System.out.println(max_road + " " + max_highest + " " + max_lowest);
					}
					trade_volume = 1;
					
					highest = highest_list.get(index);
					lowest = lowest_list.get(index);
				}
			}
			if(trade_volume == max_trade_volume)
			{
				max_road = road_list.get(index - 1);
				
				max_highest = highest;
				max_lowest = lowest;
				
				System.out.println(max_road + " " + max_highest + " " + max_lowest);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
