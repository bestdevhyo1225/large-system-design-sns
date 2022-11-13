package com.example.fastcampusmysql.util;

import java.util.List;

public record PageCursor<T>(List<T> contents, CursorRequest nextCursorRequest) {
}
