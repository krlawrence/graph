# Simple example of how you can connect to a Gremlin Server and
# issue queries from a Ruby application.

require 'net/http'
require 'uri'
require 'json'

uri = URI.parse("http://localhost:8182")

request = Net::HTTP::Post.new(uri)
req_options = { use_ssl: uri.scheme == "https", }

#query = {"gremlin" => "g.V().has('code','AUS')"}
query = {"gremlin" => "g.V().has('code','AUS').out().count()"}
request.body = JSON.dump(query)

response = Net::HTTP.start(uri.hostname, uri.port, req_options) do |http|
  http.request(request)
end

puts "Response code from the server was #{response.code}"
puts response.body     
