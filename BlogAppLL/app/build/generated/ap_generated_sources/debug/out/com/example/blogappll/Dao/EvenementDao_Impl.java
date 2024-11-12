package com.example.blogappll.Dao;

import android.database.Cursor;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.blogappll.Entity.Evenement;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class EvenementDao_Impl implements EvenementDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Evenement> __insertionAdapterOfEvenement;

  private final EntityDeletionOrUpdateAdapter<Evenement> __deletionAdapterOfEvenement;

  private final EntityDeletionOrUpdateAdapter<Evenement> __updateAdapterOfEvenement;

  public EvenementDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfEvenement = new EntityInsertionAdapter<Evenement>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `Evenement` (`id`,`title`,`description`,`lieu`,`imageUri`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Evenement value) {
        stmt.bindLong(1, value.id);
        if (value.title == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.title);
        }
        if (value.description == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.description);
        }
        if (value.lieu == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.lieu);
        }
        if (value.imageUri == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.imageUri);
        }
      }
    };
    this.__deletionAdapterOfEvenement = new EntityDeletionOrUpdateAdapter<Evenement>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `Evenement` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Evenement value) {
        stmt.bindLong(1, value.id);
      }
    };
    this.__updateAdapterOfEvenement = new EntityDeletionOrUpdateAdapter<Evenement>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `Evenement` SET `id` = ?,`title` = ?,`description` = ?,`lieu` = ?,`imageUri` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Evenement value) {
        stmt.bindLong(1, value.id);
        if (value.title == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.title);
        }
        if (value.description == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.description);
        }
        if (value.lieu == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.lieu);
        }
        if (value.imageUri == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.imageUri);
        }
        stmt.bindLong(6, value.id);
      }
    };
  }

  @Override
  public void insert(final Evenement Evenement) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfEvenement.insert(Evenement);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final Evenement Evenement) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfEvenement.handle(Evenement);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(final Evenement Evenement) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfEvenement.handle(Evenement);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<Evenement> getAllEvenements() {
    final String _sql = "SELECT * FROM Evenement";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
      final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
      final int _cursorIndexOfLieu = CursorUtil.getColumnIndexOrThrow(_cursor, "lieu");
      final int _cursorIndexOfImageUri = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUri");
      final List<Evenement> _result = new ArrayList<Evenement>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Evenement _item;
        _item = new Evenement();
        _item.id = _cursor.getInt(_cursorIndexOfId);
        if (_cursor.isNull(_cursorIndexOfTitle)) {
          _item.title = null;
        } else {
          _item.title = _cursor.getString(_cursorIndexOfTitle);
        }
        if (_cursor.isNull(_cursorIndexOfDescription)) {
          _item.description = null;
        } else {
          _item.description = _cursor.getString(_cursorIndexOfDescription);
        }
        if (_cursor.isNull(_cursorIndexOfLieu)) {
          _item.lieu = null;
        } else {
          _item.lieu = _cursor.getString(_cursorIndexOfLieu);
        }
        if (_cursor.isNull(_cursorIndexOfImageUri)) {
          _item.imageUri = null;
        } else {
          _item.imageUri = _cursor.getString(_cursorIndexOfImageUri);
        }
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public Evenement getEvenementById(final int id) {
    final String _sql = "SELECT * FROM Evenement WHERE id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
      final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
      final int _cursorIndexOfLieu = CursorUtil.getColumnIndexOrThrow(_cursor, "lieu");
      final int _cursorIndexOfImageUri = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUri");
      final Evenement _result;
      if(_cursor.moveToFirst()) {
        _result = new Evenement();
        _result.id = _cursor.getInt(_cursorIndexOfId);
        if (_cursor.isNull(_cursorIndexOfTitle)) {
          _result.title = null;
        } else {
          _result.title = _cursor.getString(_cursorIndexOfTitle);
        }
        if (_cursor.isNull(_cursorIndexOfDescription)) {
          _result.description = null;
        } else {
          _result.description = _cursor.getString(_cursorIndexOfDescription);
        }
        if (_cursor.isNull(_cursorIndexOfLieu)) {
          _result.lieu = null;
        } else {
          _result.lieu = _cursor.getString(_cursorIndexOfLieu);
        }
        if (_cursor.isNull(_cursorIndexOfImageUri)) {
          _result.imageUri = null;
        } else {
          _result.imageUri = _cursor.getString(_cursorIndexOfImageUri);
        }
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
