from django.contrib import admin
from .models import image_post,accelometer_post,Vote_table_accelometer_post,Vote_table,app_User

# Register your models here.


admin.site.register(image_post)
admin.site.register(accelometer_post)
admin.site.register(Vote_table_accelometer_post)
admin.site.register(Vote_table)
admin.site.register(app_User)