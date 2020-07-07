from django.db import models
from django.utils import timezone
from django.urls import reverse
# Create your models here.


class app_User(models.Model):
    first_name=models.CharField(max_length=50)
    last_name=models.CharField(max_length=50)
    email_id=models.EmailField()
    password=models.CharField(max_length=100)
    phone_number=models.IntegerField()

class image_post(models.Model):

    image=models.FileField(null=True,blank=True)
    title = models.CharField(max_length=200)
    Description=models.CharField(max_length=200)
    app_user_field=models.ForeignKey(app_User,on_delete=models.CASCADE,default="")
    latitude=models.DecimalField(max_digits=10,decimal_places=8,null=True)
    longitude=models.DecimalField(max_digits=11,decimal_places=8,null=True)
    created_date = models.DateTimeField(default=timezone.now)
    published_date = models.DateTimeField(blank=True, null=True)
    status=models.CharField(default="Waiting for Acknowledgement",max_length=50)
    vote_count=models.IntegerField(default=0)
    address=models.CharField(max_length=200,blank=True,null=True)

    def __str__(self):
        return self.Description

    def publish(self):

        self.published_date = timezone.now()
        self.save()

    def approve_comments(self):
        return self.comments.filter(approved_comment=True)

    def get_absolute_url(self):
        return reverse("post_detail",kwargs={'pk':self.pk})


class accelometer_post(models.Model):

    x_coordinate=models.DecimalField(max_digits=20, decimal_places=18, null=True)
    y_coordinate = models.DecimalField(max_digits=20, decimal_places=18, null=True)
    z_coordinate = models.DecimalField(max_digits=20, decimal_places=18, null=True)

    title = models.CharField(max_length=200)
    Description = models.CharField(max_length=200)
    app_user_field= models.ForeignKey(app_User, on_delete=models.CASCADE,default="")
    latitude = models.DecimalField(max_digits=10, decimal_places=8, null=True)
    longitude = models.DecimalField(max_digits=11, decimal_places=8, null=True)
    created_date = models.DateTimeField(default=timezone.now)
    published_date = models.DateTimeField(blank=True, null=True)
    status = models.CharField(default="Waiting for Acknowledgement", max_length=50)
    vote_count = models.IntegerField(default=0)
    address = models.CharField(max_length=200, blank=True, null=True)



# class Vote(models.Model):
#     post=models.ForeignKey(Post,on_delete=models.CASCADE)
#     voted_by=models.ForeignKey(User,on_delete=models.CASCADE)



class Vote_table(models.Model):
    user_id=models.IntegerField()
    post_id=models.IntegerField()

    class Meta:
        unique_together = ('user_id', 'post_id',)


class Vote_table_accelometer_post(models.Model):
    user_id=models.IntegerField()
    post_id=models.IntegerField()

    class Meta:
        unique_together = ('user_id', 'post_id',)

