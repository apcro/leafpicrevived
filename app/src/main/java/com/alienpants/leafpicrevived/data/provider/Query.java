package com.alienpants.leafpicrevived.data.provider;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import java.util.Arrays;

public class Query {

    public Uri uri;
    public String[] projection;
    public String selection;
    public String[] args;
    public String sort;
    public boolean ascending;
    public int limit;

    Query(Builder builder) {
        uri = builder.uri;
        projection = builder.projection;
        selection = builder.selection;
        args = builder.getStringArgs();
        sort = builder.sort;
        ascending = builder.ascending;
        limit = builder.limit;
    }

    public Cursor getCursor(ContentResolver cr) {
        return cr.query(uri, projection, selection, args, hack());
    }

    private String hack() {
        if (sort == null && limit == -1) return null;

        StringBuilder builder = new StringBuilder();
        if (sort != null)
            builder.append(sort);

            // Sorting by Relative Position
            // ORDER BY 1
            // sort by the first column in the PROJECTION
            // otherwise the LIMIT should not work
        else builder.append(1);

        builder.append(" ");

        if (!ascending)
            builder.append("DESC").append(" ");

        if (limit != -1)
            builder.append("LIMIT").append(" ").append(limit);

        return builder.toString();
    }

    @Override
    public String toString() {
        return "Query{" +
                "\nuri=" + uri +
                "\nprojection=" + Arrays.toString(projection) +
                "\nselection='" + selection + '\'' +
                "\nargs=" + Arrays.toString(args) +
                "\nsortMode='" + sort + '\'' +
                "\nascending='" + ascending + '\'' +
                "\nlimit='" + limit + '\'' +
                '}';
    }

    public static final class Builder {
        Uri uri = null;
        String[] projection = null;
        String selection = null;
        Object[] args = null;
        String sort = null;
        int limit = -1;
        boolean ascending = false;

        public Builder() {
        }

        public Builder uri(Uri val) {
            uri = val;
            return this;
        }

        public Builder projection(String[] val) {
            projection = val;
            return this;
        }

        public Builder selection(String val) {
            selection = val;
            return this;
        }

        public Builder args(Object... val) {
            args = val;
            return this;
        }

        public Builder sort(String val) {
            sort = val;
            return this;
        }

        public Builder limit(int val) {
            limit = val;
            return this;
        }

        public Builder ascending(boolean val) {
            ascending = val;
            return this;
        }

        public Query build() {
            return new Query(this);
        }

        public String[] getStringArgs() {
            return Arrays.stream(args).map(Object::toString).toArray(String[]::new);
        }
    }
}
