"""pothole_detection URL Configuration

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/2.2/topics/http/urls/
Examples:
Function views
    1. Add an import:  from my_app import views
    2. Add a URL to urlpatterns:  path('', views.home, name='home')
Class-based views
    1. Add an import:  from other_app.views import Home
    2. Add a URL to urlpatterns:  path('', Home.as_view(), name='home')
Including another URLconf
    1. Import the include() function: from django.urls import include, path
    2. Add a URL to urlpatterns:  path('blog/', include('blog.urls'))
"""
from django.contrib import admin
from django.urls import path
from django.contrib.auth import views as auth_views
from django.urls import path
from pothole_detection_app import views
# from users import views as user_views
from django.conf import settings
from django.conf.urls.static import static
from django.conf.urls import url, include

urlpatterns = [

    path('admin/', admin.site.urls),
    # path("",views.index,name="index"),
    path('', include('pothole_detection_app.urls')),


    #URLS API POSTS
    #
    path("api/listallimagepost", views.image_postList.as_view(), name="post_listAPI"),
    path("api/listallaccpost", views.accelometer_postList.as_view(), name="post_listAPI"),
    path("api/uploadimagepost/", views.send_image_post, name="post_listAPIModified"),
    path("api/uploadaccpost/", views.send_accelometer_post, name="post_listAPIModified"),
    path("api/deleteimagepost/<int:id>/", views.delete_image_post, name="delete post"),
    path("api/deleteaccpost/<int:id>/", views.delete_accelometer_post, name="delete post"),
    #





    # #URLS API USER
    #
    path("api/listallusers", views.app_UserList.as_view(), name="Garbage_UserListAPI"),
    #
    #  #not sure about this api
    # # path("postAPI/<int:pk>/", views.PostDetail.as_view(), name="post_details"),
    # # path("postAPI/<int:pk>/vote/", views.CreateVote.as_view(), name="create_vote"),
    #
    path("api/appuser/login/", views.validate_app_User_view, name="validate_Garbage_User"),
    path("api/appuser/register/", views.register_app_User, name="Register_Garbage_User"),

    path("api/userlikedimagepost/<int:uid>/", views.liked_image_post, name="getuserslikedspost"),
    path("api/userlikedaccpost/<int:uid>/", views.liked_accelometer_post, name="getuserslikedspost"),


    path("api/getuserimagepost/<int:uid>/", views.get_users_image_post, name="getuserspost"),
    path("api/getuseraccpost/<int:uid>/", views.get_users_accelometer_post, name="getuserspost"),


    path("api/downvoteimagepost/", views.downvote_image_post, name="downvote"),
    path("api/downvoteaccpost/", views.downvote_accelometer_post, name="downvote"),


    path("api/filterimagepost/", views.filter_image_posts, name="filterpost"),
    path("api/filteraccpost/", views.filter_accelometer_posts, name="filterpost"),


    # path("api/updateimagepostStatus/", views.update_image_post_status, name="updatestatus"),
    # path("api/updateaccpostStatus/", views.update_acc_post_status, name="updatestatus"),




    # #API VOTES
    #
    path("api/upvoteimagepost/", views.upvote_image_post_view, name="Upvote"),
    path("api/upvoteaccpost/", views.upvote_acc_post_view, name="Upvote"),

    path("api/upvotelistimagepost/", views.Vote_table_list_image_post.as_view(), name="UpvoteTable"),#gives upvote table
    path("api/upvotelistaccpost/", views.Vote_table_list_acc_post.as_view(), name="UpvoteTable"),#gives upvote table

]



if settings.DEBUG:
    urlpatterns+=static(settings.STATIC_URL, document_root=settings.STATIC_ROOT)
    urlpatterns+= static(settings.MEDIA_URL, document_root=settings.MEDIA_ROOT)


