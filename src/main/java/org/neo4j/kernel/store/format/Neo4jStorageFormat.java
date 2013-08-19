
package org.neo4j.kernel.store.format;

import org.neo4j.kernel.file.Page;


/**
 * WARNING: This file is automatically generated, do not change this file, your changes will be overwritten.
 * 
 */
public class Neo4jStorageFormat {


    public final static class NodeItem {


        public final void create(final Page page, final int itemOffset, final long createdTxId, final long removedTxId, final long firstRelationshipItemNo, final long firstRelationshipPage) {
        }

        public final int sizeOf() {
            return (((0 + 4)+ 4)+ 6);
        }

        public final void setCreatedTxId(Page page, int itemOffset, int value) {
            page.putInt((itemOffset + 0), value);
        }

        public final long getCreatedTxId(Page page, int itemOffset) {
            return page.getInt((itemOffset + 0));
        }

        public final void setRemovedTxId(Page page, int itemOffset, int value) {
            page.putInt((itemOffset + 4), value);
        }

        public final long getRemovedTxId(Page page, int itemOffset) {
            return page.getInt((itemOffset + 4));
        }

        public final void setFirstRelationshipItemNo(Page page, int itemOffset, short value) {
            page.putShort((itemOffset +(8 + 0)), value);
        }

        public final long getFirstRelationshipItemNo(Page page, short itemOffset) {
            return page.getShort((itemOffset +(8 + 0)));
        }

        public final void setFirstRelationshipPage(Page page, int itemOffset, int value) {
            page.putInt((itemOffset +(8 + 2)), value);
        }

        public final long getFirstRelationshipPage(Page page, int itemOffset) {
            return page.getInt((itemOffset +(8 + 2)));
        }

    }

    public final static class RelationshipItem {


        public final void create(final Page page, final int itemOffset, final long createdTxId, final long removedTxId, final long fromNodeItemNo, final long fromNodePage, final long toNodeItemNo, final long toNodePage, final long nextFromNodeRelationshipItemNo, final long nextFromNodeRelationshipPage, final long nextToNodeRelationshipItemNo, final long nextToNodeRelationshipPage) {
        }

        public final int sizeOf() {
            return ((((((0 + 4)+ 4)+ 6)+ 6)+ 6)+ 6);
        }

        public final void setCreatedTxId(Page page, int itemOffset, int value) {
            page.putInt((itemOffset + 0), value);
        }

        public final long getCreatedTxId(Page page, int itemOffset) {
            return page.getInt((itemOffset + 0));
        }

        public final void setRemovedTxId(Page page, int itemOffset, int value) {
            page.putInt((itemOffset + 4), value);
        }

        public final long getRemovedTxId(Page page, int itemOffset) {
            return page.getInt((itemOffset + 4));
        }

        public final void setFromNodeItemNo(Page page, int itemOffset, short value) {
            page.putShort((itemOffset +(8 + 0)), value);
        }

        public final long getFromNodeItemNo(Page page, short itemOffset) {
            return page.getShort((itemOffset +(8 + 0)));
        }

        public final void setFromNodePage(Page page, int itemOffset, int value) {
            page.putInt((itemOffset +(8 + 2)), value);
        }

        public final long getFromNodePage(Page page, int itemOffset) {
            return page.getInt((itemOffset +(8 + 2)));
        }

        public final void setToNodeItemNo(Page page, int itemOffset, short value) {
            page.putShort((itemOffset +(14 + 0)), value);
        }

        public final long getToNodeItemNo(Page page, short itemOffset) {
            return page.getShort((itemOffset +(14 + 0)));
        }

        public final void setToNodePage(Page page, int itemOffset, int value) {
            page.putInt((itemOffset +(14 + 2)), value);
        }

        public final long getToNodePage(Page page, int itemOffset) {
            return page.getInt((itemOffset +(14 + 2)));
        }

        public final void setNextFromNodeRelationshipItemNo(Page page, int itemOffset, short value) {
            page.putShort((itemOffset +(20 + 0)), value);
        }

        public final long getNextFromNodeRelationshipItemNo(Page page, short itemOffset) {
            return page.getShort((itemOffset +(20 + 0)));
        }

        public final void setNextFromNodeRelationshipPage(Page page, int itemOffset, int value) {
            page.putInt((itemOffset +(20 + 2)), value);
        }

        public final long getNextFromNodeRelationshipPage(Page page, int itemOffset) {
            return page.getInt((itemOffset +(20 + 2)));
        }

        public final void setNextToNodeRelationshipItemNo(Page page, int itemOffset, short value) {
            page.putShort((itemOffset +(26 + 0)), value);
        }

        public final long getNextToNodeRelationshipItemNo(Page page, short itemOffset) {
            return page.getShort((itemOffset +(26 + 0)));
        }

        public final void setNextToNodeRelationshipPage(Page page, int itemOffset, int value) {
            page.putInt((itemOffset +(26 + 2)), value);
        }

        public final long getNextToNodeRelationshipPage(Page page, int itemOffset) {
            return page.getInt((itemOffset +(26 + 2)));
        }

    }

}