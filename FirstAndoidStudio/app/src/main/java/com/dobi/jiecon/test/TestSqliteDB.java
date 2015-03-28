package com.dobi.jiecon.test;

import com.dobi.jiecon.data.FamilyMember;
import com.dobi.jiecon.database.sqlite.SqliteBase;

import junit.framework.TestCase;

import java.util.List;

public class TestSqliteDB extends TestCase {
    String name = "Jack";
    String phone = "837748485";
    String contacts_name_2 = "John";
    String contacts_phone_2 = "98737364";
    String contacts_name_3 = "Mike";

    public void test_update_add_unilateral_friend() {
        FamilyMember inputData = new FamilyMember();
        inputData.setName(name);
        inputData.setPhone(phone);
        SqliteBase.update_add_unilateral_family(inputData);
        FamilyMember outputData = SqliteBase.get_unilateral_friend(phone);
        assertEquals(outputData.getName(),name);
        assertEquals(outputData.getPhone(),phone);
	}
    public void test_get_unilateral_friend(){
        SqliteBase.update_add_unilateral_family(newFamilyMember(name, phone));
        SqliteBase.update_add_unilateral_family(newFamilyMember(contacts_name_2, contacts_phone_2));
        List<FamilyMember> ret = SqliteBase.get_unilateral_families();
        assertEquals(ret.get(0).getName(), name);
        assertEquals(ret.get(0).getPhone(), phone);
        assertEquals(ret.get(1).getName(), contacts_name_2);
        assertEquals(ret.get(1).getPhone(), contacts_phone_2);
        SqliteBase.update_add_unilateral_family(newFamilyMember(contacts_name_3, contacts_phone_2));
        ret = SqliteBase.get_unilateral_families();
        assertEquals(ret.get(0).getName(), name);
        assertEquals(ret.get(0).getPhone(), phone);
        assertEquals(ret.get(1).getName(), contacts_name_3);
        assertEquals(ret.get(1).getPhone(),contacts_phone_2);
    }
    private FamilyMember newFamilyMember(String name, String phone){
        FamilyMember ret = new FamilyMember();
        ret.setName(name);
        ret.setPhone(phone);
        return ret;
    }

    public void test_get_userid_from_relation_list(){
        assertEquals(SqliteBase.get_userid_from_relation_list("137611111"),"100004");
        assertEquals(SqliteBase.get_userid_from_relation_list("08032070208"),"100009");
        assertEquals(SqliteBase.get_userid_from_relation_list("9984495"), null);
    }
}
