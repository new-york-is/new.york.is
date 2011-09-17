# glennpratt@gmail.com
#

define :nginx_app, :template => "default-site.erb" do

  application_name = params[:name]

  include_recipe "nginx"

  template "#{node[:nginx][:dir]}/sites-available/#{application_name}.conf" do
    source params[:template]
    owner "root"
    group "root"
    mode 0644
    if params[:cookbook]
      cookbook params[:cookbook]
    end
    variables(
      :application_name => application_name,
      :params => params
    )
    if File.exists?("#{node[:nginx][:dir]}/sites-enabled/#{application_name}.conf")
      notifies :reload, resources(:service => "nginx"), :delayed
    end
  end

  nginx_site "#{params[:name]}.conf" do
    enable enable_setting
  end
end
