package org.library;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class ArticleRepository extends Repository<Article> {

    /**
     * Creates a new instance of repository.
     *
     * @param table    Table name associated with the underlying resource (e.g. ”account”).
     * @param database A database instance used to submit queries to the database.
     */
    public ArticleRepository(Database database) {
        super("artikel_forfattare", database);
    }


    // Create an article from a result set.
    private Article articleFrom(ResultSet resultSet) throws Exception {
        int articleType = resultSet.getInt("artikelTyp_idartikelTyp");
        // Switch for different parameters on different article types.
        switch (articleType) {
            case 1: {
                ArrayList<String> authors = new ArrayList<>();
                resultSet.getString("namn");
                return new Book(
                        resultSet.getInt("artikelID"),
                        resultSet.getString("titel"),
                        resultSet.getInt("ar"),
                        resultSet.getString("ISBN"),
                        authors,
                        resultSet.getDouble("fysiskPlats"),
                        resultSet.getInt("antal")
                );
            }
            case 2: {
                return null;
            }
            default:
                throw new Exception("Unknown article type");
        }
    }

    public ArrayList<Article> articleSearch(String attribute) throws Exception {
        return getDatabase().query("select a.artikelID, namn, titel, ar, ISBN, fysiskPlats, antal\n" +
                "from artikel_forfattare\n" +
                "join artikel a on a.artikelID = artikel_forfattare.artikelID\n" +
                "join forfattare f on f.forfattareID = artikel_forfattare.forfattareID\n" +
                "where a.artikelID in (select a.artikelID\n" +
                "                    from artikel_forfattare\n" +
                "                        join artikel a on a.artikelID = artikel_forfattare.artikelID\n" +
                "                        join forfattare f on artikel_forfattare.forfattareID = f.forfattareID\n" +
                "                    where (titel like ? or namn like ?))", preparedStatement -> {
            preparedStatement.setString(1, attribute);
            preparedStatement.setString(2, attribute);
        }, resultSet -> {
            ArrayList<Integer> idList = new ArrayList<>();
            ArrayList<Article> articles = new ArrayList<>();
            while (resultSet.next()) {
                int id = resultSet.getInt("artikelID");
                if (idList.contains(id)) {
                    for (Article article :
                            articles) {
                        if (id == article.getId() && article instanceof Book) {
                            ((Book) article).getAuthors().add(resultSet.getString("namn"));
                        }
                    }
                } else {
                    idList.add(id);
                    articles.add(articleFrom(resultSet));
                    System.out.println(articles);
                }
            }
            return articles;
        });
    }
}
