package com.example.codeswitch.network;

import com.example.codeswitch.model.Skill;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SkillDao {
    /**
     * Get list of ALL skills in the database.
     *
     * @return List<Skill>
     */
    @GET("skills")
    Call<List<Skill>> getSkillList();

    /**
     * Get list of GROUPS of skills.
     *
     * @param
     * @return
     */
    @GET("skills/groups")
    Call<List<String>> getSkillGroupList();

    /**
     * Get list of skills of a certain group.
     *
     * @return
     */
    @GET("query_skill_group")
    Call<List<Skill>> getSkillsInGroup(@Query("q") String group);
}
