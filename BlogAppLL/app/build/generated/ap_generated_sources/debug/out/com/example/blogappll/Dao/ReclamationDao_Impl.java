package com.example.blogappll.Dao;

import android.database.Cursor;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.blogappll.Entity.Reclamation;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class ReclamationDao_Impl implements ReclamationDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Reclamation> __insertionAdapterOfReclamation;

  private final EntityDeletionOrUpdateAdapter<Reclamation> __deletionAdapterOfReclamation;

  private final EntityDeletionOrUpdateAdapter<Reclamation> __updateAdapterOfReclamation;

  public ReclamationDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfReclamation = new EntityInsertionAdapter<Reclamation>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `Reclamation` (`id`,`description`,`status`,`type`) VALUES (nullif(?, 0),?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Reclamation value) {
        stmt.bindLong(1, value.id);
        if (value.description == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.description);
        }
        if (value.status == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.status);
        }
        if (value.type == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.type);
        }
      }
    };
    this.__deletionAdapterOfReclamation = new EntityDeletionOrUpdateAdapter<Reclamation>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `Reclamation` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Reclamation value) {
        stmt.bindLong(1, value.id);
      }
    };
    this.__updateAdapterOfReclamation = new EntityDeletionOrUpdateAdapter<Reclamation>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `Reclamation` SET `id` = ?,`description` = ?,`status` = ?,`type` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Reclamation value) {
        stmt.bindLong(1, value.id);
        if (value.description == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.description);
        }
        if (value.status == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.status);
        }
        if (value.type == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.type);
        }
        stmt.bindLong(5, value.id);
      }
    };
  }

  @Override
  public void insert(final Reclamation reclamation) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfReclamation.insert(reclamation);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final Reclamation reclamation) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfReclamation.handle(reclamation);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(final Reclamation reclamation) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfReclamation.handle(reclamation);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<Reclamation> getAllReclamations() {
    final String _sql = "SELECT * FROM Reclamation";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
      final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
      final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
      final List<Reclamation> _result = new ArrayList<Reclamation>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Reclamation _item;
        _item = new Reclamation();
        _item.id = _cursor.getInt(_cursorIndexOfId);
        if (_cursor.isNull(_cursorIndexOfDescription)) {
          _item.description = null;
        } else {
          _item.description = _cursor.getString(_cursorIndexOfDescription);
        }
        if (_cursor.isNull(_cursorIndexOfStatus)) {
          _item.status = null;
        } else {
          _item.status = _cursor.getString(_cursorIndexOfStatus);
        }
        if (_cursor.isNull(_cursorIndexOfType)) {
          _item.type = null;
        } else {
          _item.type = _cursor.getString(_cursorIndexOfType);
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
  public Reclamation getReclamationById(final int id) {
    final String _sql = "SELECT * FROM Reclamation WHERE id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
      final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
      final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
      final Reclamation _result;
      if(_cursor.moveToFirst()) {
        _result = new Reclamation();
        _result.id = _cursor.getInt(_cursorIndexOfId);
        if (_cursor.isNull(_cursorIndexOfDescription)) {
          _result.description = null;
        } else {
          _result.description = _cursor.getString(_cursorIndexOfDescription);
        }
        if (_cursor.isNull(_cursorIndexOfStatus)) {
          _result.status = null;
        } else {
          _result.status = _cursor.getString(_cursorIndexOfStatus);
        }
        if (_cursor.isNull(_cursorIndexOfType)) {
          _result.type = null;
        } else {
          _result.type = _cursor.getString(_cursorIndexOfType);
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
