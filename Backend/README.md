<h1>Api usage</h1>
<h2>User</h2>
<ul>
<li>
<b>[GET] url/api/v1/user/activities/enrolled :</b>
<p>Show all activities that the current logged in user is enrolled in</p>
</li>

<li>
<b>[POST] url/api/v1/user/activities/enroll/{activity_id} :</b>
<p>Enroll the current logged in user to the activity belonging to the id {activity_id}
<br>
Eg: url/api/v1/user/activities/enroll/1
</p>
</li>

<li>
<b>[POST] url/api/v1/user/activities/unroll/{activity_id} :</b>
<p>Unroll the current logged in user to the activity belonging to the id {activity_id}
<br>
Eg: url/api/v1/user/activities/unroll/1
</p>
</li>

<li>
<b>[POST] url/api/v1/user/signin :</b>
<p>Gateway for authentication (Must have an account in order to use this)
</li>

<li>
<b>[POST] url/api/v1/user/signup :</b>
<p>Gateway for creating a new account
</li>

</ul>


<h2>Activity</h2>
<ul>
<li>
<b>[GET] url/api/v1/activity/list :</b>
<p>List of all activities on the platform and all the users enrolled in it</p>
</li>

<li>
<b>[GET] url/api/v1/activity/{activity_id} :</b>
<p>List the activity belonging to the id {activity_id} and all the users enrolled in it
</p>
</li>

<li>
<b>[POST] url/api/v1/activity/add :</b>
<p>Create a brand new activity, so users can enroll, title and description must be provided
</p>
</li>

</ul>

<p>Documented by yannick fumukani</p>