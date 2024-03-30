const http = require('http');

const server = http.createServer((req, res) => {
  // Set the response HTTP header with HTTP status and Content type
  res.writeHead(200, {'Content-Type': 'text/plain'});

  // Write the message "Welcome to 2022" to response
  res.write('Welcome to 2022\n');

  // Get user agent information from the request headers
  const userAgent = req.headers['user-agent'];

  // Write user agent information to response
  res.write(`User Agent: ${userAgent}\n`);

  // End the response
  res.end();
});

// Server listens on port 3000
const PORT = process.env.PORT || 3000;
server.listen(PORT, () => {
  console.log(`Server is running on port ${PORT}`);
});
