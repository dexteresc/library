require 'net/http'
require 'json'

class String
  def truncate(max)
    length > max ? "#{self[0...max]}" : self
  end
end

def fetch(uri_str, limit = 10)
    # You should choose better exception.
    raise ArgumentError, 'HTTP redirect too deep' if limit == 0
  
    url = URI.parse(uri_str)
    req = Net::HTTP::Get.new(url.path, { 'User-Agent' => 'Mozilla/5.0 (etc...)' })
    response = Net::HTTP.start(url.host, url.port, use_ssl: true) { |http| http.request(req) }
    case response
    when Net::HTTPSuccess     then response.body
    when Net::HTTPRedirection then fetch(response['location'], limit - 1)
    else
      response.error!
    end
end

def get_book(key)
    url = 'https://openlibrary.org/api/books?bibkeys=<KEY>&jscmd=details&format=json'.gsub('<KEY>', key.gsub('/works/', ''))
    puts url
    body = fetch(url)
    return JSON.parse(body)
end

id = 15
author_id = 15
["architecture", "art__art_instruction", "history_of_art__art__design_styles", "dance", "design", "fashion", "film", "graphic_design", "music", "music_theory", "painting__paintings", "photography", "bears", "cats", "kittens", "dogs", "puppies", "fiction", "fantasy", "historical_fiction", "horror", "humor", "literature", "magic", "mystery_and_detective_stories", "plays", "poetry", "romance", "science_fiction", "short_stories", "thriller", "young_adult_fiction", "sciencemathematics", "biology", "chemistry", "mathematics", "physics", "programming", "business", "management", "entrepreneurship", "business__economics", "success_in_business", "finance", "juvenile_fiction", "juvenile_literature", "stories_in_rhyme", "infancy", "bedtime", "picture_books", "history", "ancient_civilization", "archaeology", "anthropology"].each do |subject|
    list = JSON.parse(fetch("https://openlibrary.org/subjects/#{subject}.json"))['works']
    lf = File.open("books/#{subject}.json", 'w+')
    lf.write(list.to_json)
    lf.close()

    list.each do |work|
        f = File.open("exports/#{subject}_#{work['key'].gsub('/works/', '')}.sql", 'w+')
        print "#{work['key']} ..."
        id += 1
        title = "#{work['title']}"
        classification = "#{work['subject'].join(', ').truncate(128)}"
        isbn = ""
        if work['availability'] != nil
            isbn = "#{work['availability']['isbn']}"
        end
        summary = "This title (#{work['key']}) was imported from Open Library."
        print " media ."
        f.puts("INSERT INTO media (id, title, classification, summary, location) VALUES (#{id}, '#{title}', '#{classification}', '#{summary}', '');\n" +
               "INSERT INTO book (media_id, isbn) VALUES (#{id}, '#{isbn}');\n")
        print " book ."
        print " authors ."
        work['authors'].each do |author|
            print "."
            author_id += 1
            given_name = "" 
            family_name = ""
            if author['name'].include? " "
              given_name = "#{author['name'].split(' ', 2)[0]}"
              family_name = "#{author['name'].split(' ', 2)[1]}"
            end
            f.puts("INSERT INTO author (id, given_name, family_name) VALUES (#{author_id}, '#{given_name}', '#{family_name}');\n" + 
                   "INSERT INTO media_author (media_id, author_id) VALUES (#{id}, #{author_id});\n")
        end
        print " items ."
        f.puts("INSERT INTO media_item (media_id, media_type_id) VALUES(#{id}, 4);\n")
        rand(2..6).times do
            print "."
            f.puts("INSERT INTO media_item (media_id, media_type_id) VALUES(#{id}, 2);\n")
        end
        print " finishing ..."
        f.close()
        print " done"
        puts ""
        sleep 0.1
    end
end

# list.each do |x|
#     book = get_book(x['key'])
#     f = File.open('books/'+x['key'].gsub('/works/', '')+'.json', 'w+')
#     f.write(book.to_json)
#     f.close()
#     exit
# end
