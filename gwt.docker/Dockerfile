FROM nginx
ADD ./default.conf /etc/nginx/conf.d/default.conf
COPY --chown=nginx:nginx target/war /var/www
RUN chmod -R +rx /var/www

