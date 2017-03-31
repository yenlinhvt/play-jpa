$(function() {
  $.get("/books", function(books) {
    renderBooks(books);
  });

  $("#searchBtn").click(function() {
    var searchKey = $("#key").val();
    $.get("/searchBooks", {key: searchKey}, function(books) {
      renderBooks(books);
    });
  });

  $("#editBtn").click(function() {
    $.ajax({
      url: '/book',
      method: 'PUT',
      data: $('#bookForm').serialize()
    }).done(function() {
      $("#isbnNo").val("");
      $("#title").val("");
      $("#author").val("");
      $("#id").val("");

      $("#submitBtn").show();
      $("#editBtn").hide();

      $("#key").val("");
      $("#searchBtn").click();
    });
  });

  $(document).on('click', '.view-book', function() {
    var id = $(this).attr("data-id");
    $.get("/book", {id: id}, function(book) {
      renderBook(book);
    });
  });

  $(document).on('click', '.delete-book', function() {
      var id = $(this).attr("data-id"),
          book = $(this).parent().parent();
      $.ajax({
        url: '/book?id=' + id,
        method: 'DELETE',
      }).done(function() {
        book.remove();
      });
    });
});

function renderBooks(books) {
  $("#books > tbody").html('');
  $(books).each(function( index,item ) {
    $("#books > tbody").append('<tr class="book">' +
                               "<td>" + item.id + "</td>" +
                               "<td>" + item.title + "</td>" +
                               "<td>" + item.author + "</td>" +
                               "<td><button class='view-book btn btn-primary' data-id='" + item.id + "'>View</button></td>" +
                               "<td><button class='delete-book btn btn-primary' data-id='" + item.id + "'>Delete</button></td>" +
                               "</tr>");
  });
}

function renderBook(book) {
  $("#isbnNo").val(book.isbnNo);
  $("#title").val(book.title);
  $("#author").val(book.author);
  $("#id").val(book.id);

  $("#submitBtn").hide();
  $("#editBtn").show();
}


