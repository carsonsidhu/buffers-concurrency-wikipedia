package cpen221.mp3.wikimediator.queries;

import org.antlr.v4.runtime.tree.ParseTree;
import org.fastily.jwiki.core.Wiki;
import org.fastily.jwiki.dwrap.Contrib;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class QueryEvaluator extends queryGrammarBaseVisitor {
    private static final String PAGE = "page";
    private static final String AUTHOR = "author";
    private static final String CATEGORY = "category";

    private static final String CATEGORY_PREFIX = "Category: ";

    String returnType;
    Wiki wiki;

    /**
     * Visit a parse tree produced by {@link queryGrammarParser#query}.
     * @param ctx the parse tree
     *            must be of type QueryContext
     * @param w the wiki object used by the WikiMediator calling this method
     *          is not null
     * @return a list of Strings that satisfies the given query (see executeQuery spec for specifics)
     */
    public List<String> visitQuery(queryGrammarParser.QueryContext ctx, Wiki w) {
        returnType = ctx.ITEM().toString();
        wiki = w;

        if (ctx.SORTED() == null) {
            return this.visitCondition(ctx.condition());
        }

        List<String> returnList = this.visitCondition(ctx.condition());
        returnList.sort(Comparator.naturalOrder());

        if (ctx.SORTED().getText().equals("desc")) {
            Collections.reverse(returnList);
        }

        return returnList;
    }

    /**
     * Visit a parse tree produced by {@link queryGrammarParser#query}.
     * @param ctx the parse tree
     *            must be of type ConditionContext
     * @return a list of Strings that satisfies the given condition (see executeQuery spec for specifics)
     */
    @Override
    public List<String> visitCondition(queryGrammarParser.ConditionContext ctx) {
        if (ctx.getChildCount() == 1) {
            return visitSimple_condition(
                (queryGrammarParser.Simple_conditionContext) ctx.getChild(0));
        }
        String operator = ctx.OPERATOR().getText();
        List<String> leftConditionList = new ArrayList<>();
        List<String> rightConditionList = new ArrayList<>();
        boolean leftConditionFound = false;


        for (ParseTree p : ctx.children) {

            if (p instanceof queryGrammarParser.ConditionContext) {
                if (!leftConditionFound) {
                    leftConditionList = this.visitCondition(
                        (queryGrammarParser.ConditionContext) p);
                    leftConditionFound = true;
                } else {
                    rightConditionList = this.visitCondition(
                        (queryGrammarParser.ConditionContext) p);
                }
            }
        }


        if (operator.equals("or")) {
            List<String> returnList = new ArrayList<>();
            returnList.addAll(leftConditionList);
            returnList.addAll(rightConditionList);

            return returnList;
        }
        if (operator.equals("and")) {
            List<String> finalRightConditionList = rightConditionList;
            return leftConditionList.stream().filter(s -> finalRightConditionList
                .contains(s)).collect(
                Collectors.toList());

        }
        return Collections.emptyList();
    }

    /**
     * Visit a parse tree produced by {@link queryGrammarParser#query}.
     * @param ctx the parse tree
     *            must be of type Simple_conditionContext
     * @return a list of Strings that satisfies the given simple condition (see executeQuery spec for specifics)
     */
    @Override
    public List<String> visitSimple_condition(queryGrammarParser.Simple_conditionContext ctx) {
        String request = ctx.ITEM().getText();
        String criteria = ctx.STRING().toString();
        criteria = criteria.substring(1, criteria.length() - 1);

        switch (request) {

            case (AUTHOR):
                return authorIs(criteria);
            case (CATEGORY):
                return categoryIs(criteria);
            case (PAGE):
                return pageIs(criteria);
        }

        return Collections.emptyList();

    }

    /**
     * returns the titles and categories associated with a given author
     *
     * @param author the author of a given page
     * @return a list of titles or categories corresponding to a given author
     */
    private List<String> authorIs(String author) {
        if (returnType.equals(AUTHOR)) {
            return List.of(author);
        }
        if (returnType.equals(CATEGORY)) {
            List<Contrib> editList = wiki.getContribs(author, -1, false, false);
            List<String> titleList = editList.stream().map(c -> c.title).
                filter(t -> wiki.getLastEditor(t).equals(author)).collect(Collectors.toList());
            titleList = new ArrayList<>(new HashSet<>(titleList));

            List<String> categoryList = new ArrayList<>();
            for (String s : titleList) {
                categoryList.addAll(wiki.getCategoriesOnPage(s));
            }
            categoryList = new ArrayList<>(new HashSet<>(categoryList));
            return categoryList;
        }
        if (returnType.equals(PAGE)) {
            List<Contrib> editList = wiki.getContribs(author, -1, false, false);
            List<String> titleList = editList.stream().map(c -> c.title).
                filter(t -> wiki.getLastEditor(t).equals(author)).collect(Collectors.toList());

            titleList = new ArrayList<>(new HashSet<>(titleList));
            return titleList;
        }

        return Collections.emptyList();
    }

    /**
     * returns authors and titles associated with a given category
     *
     * @param category the category of a given page, or associated with an author
     * @return a list of authors or pages corresponding to a provided category
     */
    private List<String> categoryIs(String category) {
        if (returnType.equals(AUTHOR)) {
            String wikiTerm = CATEGORY_PREFIX.concat(category);
            List<String> pageList = wiki.getCategoryMembers(wikiTerm);
            return pageList.stream().map(s -> wiki.getLastEditor(s)).collect(Collectors.toList());
        }
        if (returnType.equals(CATEGORY)) {
            return List.of(category);
        }
        if (returnType.equals(PAGE)) {
            String wikiTerm = CATEGORY_PREFIX.concat(category);
            return wiki.getCategoryMembers(wikiTerm);
        }
        return Collections.emptyList();
    }

    /**
     * returns authors and categories associated with a given page
     *
     * @param page a page associated with a given author or category
     * @return a list of authors or categories corresponding to a given page
     */
    private List<String> pageIs(String page) {

        if (returnType.equals(AUTHOR)) {
            try {
                return List.of(wiki.getLastEditor(page));
            } catch (RuntimeException e) {
                return Collections.emptyList();
            }
        }
        if (returnType.equals(CATEGORY)) {
            return wiki.getCategoriesOnPage(page);
        }
        if (returnType.equals(PAGE)) {
            return List.of(page);
        }
        return Collections.emptyList();
    }
}
