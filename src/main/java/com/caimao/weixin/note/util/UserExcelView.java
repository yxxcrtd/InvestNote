package com.caimao.weixin.note.util;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import com.caimao.weixin.note.domain.User;

@SuppressWarnings("deprecation")
public class UserExcelView extends AbstractExcelView {

	@Override
	protected void buildExcelDocument(Map<String, Object> map, HSSFWorkbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
		@SuppressWarnings("unchecked")
		List<User> userList = (List<User>) map.get("userList");
		
		// 创建 sheet
		HSSFSheet sheet = workbook.createSheet();
		workbook.setSheetName(0, "第一个Sheet");
		sheet.setDefaultColumnWidth((short) 15); // 设置列的宽度
		sheet.createFreezePane(0, 1); // 冻结第一行(0是第0列，1是第一行)
		HSSFRow row = sheet.createRow((short) 0); // 第一行
		createCell(workbook, row, (short) 0, "用户ID");
		createCell(workbook, row, (short) 1, "昵称");
		createCell(workbook, row, (short) 2, "是否订阅");
		createCell(workbook, row, (short) 3, "笔记数量");
		
		int startRow = 1; // 下一行从1开始
		HSSFRow new_row = null; // 创建 Excel 的 行(row)和单元格(cell)
		for (User user : userList) {
			new_row = sheet.createRow(startRow); // 创建下一行
			createCell(workbook, new_row, (short) 0, String.valueOf(user.getUser_id()));
			createCell(workbook, new_row, (short) 1, user.getUser_nickname());
			createCell(workbook, new_row, (short) 2, String.valueOf(user.getUser_subscribe()));
			createCell(workbook, new_row, (short) 3, String.valueOf(user.getUser_note_count()));
			startRow++;
		}
		response.setContentType("APPLICATION/OCTET-STREAM");
		response.setHeader("Content-Disposition", "attachment;filename=" + new String("用户列表.xls".getBytes("UTF-8"), "ISO8859-1"));
	}

	/**
	 * 创建单元格
	 * 
	 * @param wb
	 * @param row
	 * @param column
	 * @param value
	 */
	private void createCell(HSSFWorkbook wb, HSSFRow row, short column, String value) {
		HSSFCell cell = row.createCell(column);
		cell = row.createCell(column);
		cell.setCellValue(value);
		HSSFCellStyle cellstyle = wb.createCellStyle();
		cellstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cell.setCellStyle(cellstyle);
	}

}
