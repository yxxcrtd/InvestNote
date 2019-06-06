package com.caimao.weixin.note.dao.impl;

import com.caimao.weixin.note.domain.Bank;
import com.caimao.weixin.note.domain.Capital;
import com.caimao.weixin.note.domain.Comment;
import com.caimao.weixin.note.domain.Note;
import com.caimao.weixin.note.domain.Reader;
import com.caimao.weixin.note.domain.User;
import com.caimao.weixin.note.domain.Withdraw;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RowMappers {

    public static final class ReaderOtherMapper implements RowMapper<Reader> {
        public Reader mapRow(ResultSet rs, int rowNum) throws SQLException {
            Reader reader = new Reader();
            reader.setReader_create_time(rs.getTimestamp("reader_create_time"));
            reader.setReader(reader);

            User user = new User();
            user.setUser_nickname(rs.getString("user_nickname"));
            user.setUser_header_img(rs.getString("user_header_img"));
            reader.setUser(user);

            Note note = new Note();
            note.setNote_id(rs.getInt("note_id"));
            note.setNote_stock_name(rs.getString("note_stock_name"));
            note.setNote_stock_code(rs.getString("note_stock_code"));
            note.setNote_target_day(rs.getInt("note_target_day"));
            note.setNote_increase(rs.getInt("note_increase"));
            note.setNote_open_money(rs.getDouble("note_open_money"));
            note.setNote_amount(rs.getInt("note_amount"));
            note.setNote_status(rs.getInt("note_status"));
            note.setNote_target_status(rs.getInt("note_target_status"));
            note.setNote_start_time(rs.getDate("note_start_time"));
            reader.setNote(note);
            return reader;
        }
    }
    
	public static final class ReaderOfNote implements RowMapper<Reader> {
        public Reader mapRow(ResultSet rs, int rowNum) throws SQLException {
            Reader reader = new Reader();
			reader.setReader_note_id(rs.getInt("reader_note_id"));
			reader.setCount(rs.getInt("COUNT(r.reader_note_id)"));
			Note note = new Note();
			note.setNote_stock_name(rs.getString("note_stock_name"));
			note.setNote_create_time(rs.getTimestamp("note_create_time"));
			reader.setNote(note);
			User user = new User();
			user.setUser_id(rs.getInt("user_id"));
			user.setUser_nickname(rs.getString("user_nickname"));
			user.setUser_open_id(rs.getString("user_open_id"));
			reader.setUser(user);
            return reader;
        }
    }

    public static final class ReaderUserMapper implements RowMapper<Reader> {
        public Reader mapRow(ResultSet rs, int rowNum) throws SQLException {
            Reader reader = new Reader();
            User user = new User();
            user.setUser_id(rs.getInt("user_id"));
            user.setUser_header_img(rs.getString("user_header_img"));
            reader.setUser(user);
            return reader;
        }
    }

    public static final class NoteOtherMapper implements RowMapper<Note> {
        public Note mapRow(ResultSet rs, int rowNum) throws SQLException {
            Note note = new Note();
            note.setNote_user_id(rs.getInt("note_user_id"));
            note.setNote_id(rs.getInt("note_id"));
            note.setNote_stock_name(rs.getString("note_stock_name"));
            note.setNote_stock_code(rs.getString("note_stock_code"));
            note.setNote_stock_open_price(rs.getDouble("note_stock_open_price"));
            note.setNote_target_day(rs.getInt("note_target_day"));
            note.setNote_increase(rs.getInt("note_increase"));
            note.setNote_open_money(rs.getDouble("note_open_money"));
            note.setNote_packet_money(rs.getDouble("note_packet_money"));
            note.setNote_amount(rs.getInt("note_amount"));
            note.setNote_status(rs.getInt("note_status"));
            note.setNote_target_status(rs.getInt("note_target_status"));
            note.setNote_create_time(rs.getTimestamp("note_create_time"));
            note.setNote_start_time(rs.getTimestamp("note_start_time"));
            note.setNote_end_time(rs.getTimestamp("note_end_time"));

            User user = new User();
            user.setUser_nickname(rs.getString("user_nickname"));
            note.setUser(user);

            return note;
        }
    }

	public static final class NoteMyFocusMapper implements RowMapper<Note> {
		public Note mapRow(ResultSet rs, int rowNum) throws SQLException {
			Note note = new Note();
			note.setNote_id(rs.getInt("note_id"));
			note.setNote_user_id(rs.getInt("note_user_id"));
			note.setNote_title(rs.getString("note_title"));
			note.setNote_remark(rs.getString("note_remark"));
			note.setNote_open_money(rs.getDouble("note_open_money"));
			note.setNote_target_day(rs.getInt("note_target_day"));
			note.setNote_increase(rs.getInt("note_increase"));
			note.setNote_status(rs.getInt("note_status"));
			note.setNote_target_status(rs.getInt("note_target_status"));
			note.setNote_create_time(rs.getTimestamp("note_create_time"));

			User user = new User();
			user.setUser_nickname(rs.getString("user_nickname"));
			user.setUser_header_img(rs.getString("user_header_img"));
			note.setUser(user);

			return note;
		}
	}

    public static final class WithdrawOtherMapper implements RowMapper<Withdraw> {
        public Withdraw mapRow(ResultSet rs, int rowNum) throws SQLException {
            Withdraw withdraw = new Withdraw();
            withdraw.setWithdraw_id(rs.getInt("withdraw_id"));
            withdraw.setWithdraw_batch_no(rs.getString("withdraw_batch_no"));
            withdraw.setWithdraw_user_id(rs.getInt("withdraw_user_id"));
            withdraw.setWithdraw_bank_id(rs.getInt("withdraw_bank_id"));
            withdraw.setWithdraw_money(rs.getDouble("withdraw_money"));
            withdraw.setWithdraw_status(rs.getInt("withdraw_status"));
            withdraw.setWithdraw_create_time(rs.getTimestamp("withdraw_create_time"));

            Bank bank = new Bank();
            bank.setBank_code(rs.getString("bank_code"));
            bank.setBank_name(rs.getString("bank_name"));
            bank.setBank_user_name(rs.getString("bank_user_name"));
            bank.setBank_user_code(rs.getString("bank_user_code"));
            bank.setBank_add_province(rs.getString("bank_add_province"));
            bank.setBank_add_city(rs.getString("bank_add_city"));
            bank.setBank_open_bank(rs.getString("bank_open_bank"));
            withdraw.setBank(bank);

            User user = new User();
            user.setUser_nickname(rs.getString("user_nickname"));
            user.setUser_phone(rs.getString("user_phone"));
            withdraw.setUser(user);

            return withdraw;
        }
    }

    public static final class CapitalOtherMapper implements RowMapper<Capital> {
        public Capital mapRow(ResultSet rs, int rowNum) throws SQLException {
            Capital capital = new Capital();
            capital.setCapital_id(rs.getInt("capital_id"));
            capital.setCapital_user_id(rs.getInt("capital_user_id"));
            capital.setCapital_type(rs.getInt("capital_type"));
            capital.setCapital_mount(rs.getDouble("capital_mount"));
            capital.setCapital_available(rs.getDouble("capital_available"));
            capital.setCapital_freeze(rs.getDouble("capital_freeze"));
            capital.setCapital_create_time(rs.getTimestamp("capital_create_time"));
            User user = new User();
            user.setUser_nickname(rs.getString("user_nickname"));
            user.setUser_header_img(rs.getString("user_header_img"));
            capital.setUser(user);

            return capital;
        }
    }

    public static final class ReaderMapper implements RowMapper<User> {
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setUser_header_img(rs.getString("user_header_img"));
            return user;
        }
    }

    
    public static final class CommentMapper implements RowMapper<Comment> {
		@Override
		public Comment mapRow(ResultSet rs, int rowNum) throws SQLException {
			Comment comment = new Comment();
			comment.setComment_id(rs.getInt("comment_id"));
			comment.setComment_user_id(rs.getInt("comment_user_id"));
			comment.setComment_user_nickname(rs.getString("comment_user_nickname"));
			comment.setComment_user_header_img(rs.getString("comment_user_header_img"));
			comment.setComment_stock_code(rs.getString("comment_stock_code"));
			comment.setComment_stock_name(rs.getString("comment_stock_name"));
			comment.setComment_note_remark(rs.getString("comment_note_remark"));
			comment.setComment_note_id(rs.getInt("comment_note_id"));
			comment.setComment_content(rs.getString("comment_content"));
			comment.setComment_create_time(rs.getTimestamp("comment_create_time"));
			return comment;
		}
    }
}
