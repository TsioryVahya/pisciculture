<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:set var="uri" value="${requestScope['jakarta.servlet.forward.request_uri']}" />
<div class="fixed inset-y-0 left-0 w-64 bg-gray-50 border-r border-gray-200 overflow-y-auto lg:block hidden">
    <div class="flex items-center justify-center h-16 border-b border-gray-200">
        <div class="flex items-center">
            <svg class="h-8 w-8 text-emerald-600 mr-2" fill="currentColor" viewBox="0 0 24 24">
                <path d="M2,12C2,12 5,5 12,5C15,5 18,6.5 20,8.5L22,7V17L20,15.5C18,17.5 15,19 12,19C5,19 2,12 2,12M12,17C14.76,17 17.5,15.82 19.14,14.07C18.4,14.66 17.31,15 16,15C13.79,15 12,13.21 12,11C12,10.13 12.28,9.33 12.76,8.68C11.13,8.23 9.15,8 7,8C5.9,8 4.88,8.06 3.96,8.18C4.5,9.26 5.5,10.5 7,11.5C8.5,12.5 10,13 11,13C10.45,13 10,12.55 10,12C10,11.45 10.45,11 11,11C11.55,11 12,11.45 12,12C12,12.55 11.55,13 11,13C12.1,13 13.5,12.5 15,11.5C16.5,10.5 17.5,9.26 18.04,8.18C16.5,7.42 14.36,7 12,7C7.5,7 4.14,10.07 3.1,12C4.14,13.93 7.5,17 12,17M16,10A1,1 0 0,1 17,11A1,1 0 0,1 16,12A1,1 0 0,1 15,11A1,1 0 0,1 16,10Z" />
            </svg>
            <span class="text-xl font-bold text-emerald-600">Pisciculture</span>
        </div>
    </div>
    <nav class="mt-6 px-4">
        <div class="space-y-1">
            <!-- Dashboard -->
            <a href="${pageContext.request.contextPath}/" 
               class="flex items-center px-4 py-2 text-sm font-medium ${uri == pageContext.request.contextPath or uri == (pageContext.request.contextPath.concat('/')) ? 'text-gray-900 bg-gray-200' : 'text-gray-600 hover:text-gray-900 hover:bg-gray-100'} rounded-md group">
                <svg class="mr-3 h-6 w-6 ${uri == pageContext.request.contextPath or uri == (pageContext.request.contextPath.concat('/')) ? 'text-gray-500' : 'text-gray-400 group-hover:text-gray-500'}" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6" />
                </svg>
                Dashboard
            </a>
            
            <div class="pt-4 pb-2">
                <p class="px-4 text-xs font-semibold text-gray-500 uppercase tracking-wider">Référentiels</p>
            </div>

            <!-- Races -->
            <a href="${pageContext.request.contextPath}/races" 
               class="flex items-center px-4 py-2 text-sm font-medium ${fn:contains(uri, '/races') ? 'text-gray-900 bg-gray-200' : 'text-gray-600 hover:text-gray-900 hover:bg-gray-100'} rounded-md group">
                <svg class="mr-3 h-6 w-6 ${fn:contains(uri, '/races') ? 'text-gray-500' : 'text-gray-400 group-hover:text-gray-500'}" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 11H5m14 0a2 2 0 012 2v6a2 2 0 01-2 2H5a2 2 0 01-2-2v-6a2 2 0 012-2m14 0V9a2 2 0 00-2-2M5 11V9a2 2 0 012-2m0 0V5a2 2 0 012-2h6a2 2 0 012 2v2M7 7h10" />
                </svg>
                Races de poissons
            </a>

            <!-- Étangs -->
            <a href="${pageContext.request.contextPath}/etangs" 
               class="flex items-center px-4 py-2 text-sm font-medium ${fn:contains(uri, '/etangs') ? 'text-gray-900 bg-gray-200' : 'text-gray-600 hover:text-gray-900 hover:bg-gray-100'} rounded-md group">
                <svg class="mr-3 h-6 w-6 ${fn:contains(uri, '/etangs') ? 'text-gray-500' : 'text-gray-400 group-hover:text-gray-500'}" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 7v10c0 2.21 3.582 4 8 4s8-1.79 8-4V7M4 7c0 2.21 3.582 4 8 4s8-1.79 8-4M4 7c0-2.21 3.582-4 8-4s8 1.79 8 4m0 5c0 2.21-3.582 4-8 4s-8-1.79-8-4" />
                </svg>
                Étangs
            </a>

            <!-- Nourritures -->
            <a href="${pageContext.request.contextPath}/nourritures" 
               class="flex items-center px-4 py-2 text-sm font-medium ${fn:contains(uri, '/nourritures') ? 'text-gray-900 bg-gray-200' : 'text-gray-600 hover:text-gray-900 hover:bg-gray-100'} rounded-md group">
                <svg class="mr-3 h-6 w-6 ${fn:contains(uri, '/nourritures') ? 'text-gray-500' : 'text-gray-400 group-hover:text-gray-500'}" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 15.546c.533.28.827.84.827 1.454V19a2 2 0 01-2 2h-1a2 2 0 01-2-2v-2.01a1.5 1.5 0 01.327-.946L18 15.546zM3 15.546c-.533.28-.827.84-.827 1.454V19a2 2 0 01-2 2h1a2 2 0 012-2v-2.01a1.5 1.5 0 01-.327-.946L3 15.546zM12 3a9 9 0 00-9 9c0 1.49.362 2.894 1 4.132l1.327-2.322A5 5 0 1112 17a5 5 0 01-4.327-2.49l-1.327 2.322A9 9 0 1012 3z" />
                </svg>
                Nourritures
            </a>

            <div class="pt-4 pb-2">
                <p class="px-4 text-xs font-semibold text-gray-500 uppercase tracking-wider">Gestion</p>
            </div>

            <!-- Poissons -->
            <a href="${pageContext.request.contextPath}/poissons" 
               class="flex items-center px-4 py-2 text-sm font-medium ${fn:contains(uri, '/poissons') ? 'text-gray-900 bg-gray-200' : 'text-gray-600 hover:text-gray-900 hover:bg-gray-100'} rounded-md group">
                <svg class="mr-3 h-6 w-6 ${fn:contains(uri, '/poissons') ? 'text-gray-500' : 'text-gray-400 group-hover:text-gray-500'}" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 11H5m14 0a2 2 0 012 2v6a2 2 0 01-2 2H5a2 2 0 01-2-2v-6a2 2 0 012-2m14 0V9a2 2 0 00-2-2M5 11V9a2 2 0 012-2m0 0V5a2 2 0 012-2h6a2 2 0 012 2v2M7 7h10" />
                </svg>
                Poissons
            </a>

            <!-- Affectations -->
            <a href="${pageContext.request.contextPath}/affectations" 
               class="flex items-center px-4 py-2 text-sm font-medium ${fn:contains(uri, '/affectations') ? 'text-gray-900 bg-gray-200' : 'text-gray-600 hover:text-gray-900 hover:bg-gray-100'} rounded-md group">
                <svg class="mr-3 h-6 w-6 ${fn:contains(uri, '/affectations') ? 'text-gray-500' : 'text-gray-400 group-hover:text-gray-500'}" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7h12m0 0l-4-4m4 4l-4 4m0 6H4m0 0l4 4m-4-4l4-4" />
                </svg>
                Historique Affectations
            </a>

            <!-- Alimentation -->
            <a href="${pageContext.request.contextPath}/alimentations" 
               class="flex items-center px-4 py-2 text-sm font-medium ${fn:contains(uri, '/alimentations') ? 'text-gray-900 bg-gray-200' : 'text-gray-600 hover:text-gray-900 hover:bg-gray-100'} rounded-md group">
                <svg class="mr-3 h-6 w-6 ${fn:contains(uri, '/alimentations') ? 'text-gray-500' : 'text-gray-400 group-hover:text-gray-500'}" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6.253v13m0-13C10.832 5.477 9.246 5 7.5 5S4.168 5.477 3 6.253v13C4.168 18.477 5.754 18 7.5 18s3.332.477 4.5 1.253m0-13C13.168 5.477 14.754 5 16.5 5c1.747 0 3.332.477 4.5 1.253v13C19.832 18.477 18.247 18 16.5 18c-1.746 0-3.332.477-4.5 1.253" />
                </svg>
                Alimentation
            </a>

            <!-- Reporting -->
            <a href="${pageContext.request.contextPath}/report/etangs" 
               class="flex items-center px-4 py-2 text-sm font-medium ${fn:contains(uri, '/report/etangs') ? 'text-gray-900 bg-gray-200' : 'text-gray-600 hover:text-gray-900 hover:bg-gray-100'} rounded-md group">
                <svg class="mr-3 h-6 w-6 ${fn:contains(uri, '/report/etangs') ? 'text-gray-500' : 'text-gray-400 group-hover:text-gray-500'}" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z" />
                </svg>
                Reporting Étangs
            </a>

            <a href="${pageContext.request.contextPath}/report/poissons" 
               class="flex items-center px-4 py-2 text-sm font-medium ${fn:contains(uri, '/report/poissons') ? 'text-gray-900 bg-gray-200' : 'text-gray-600 hover:text-gray-900 hover:bg-gray-100'} rounded-md group">
                <svg class="mr-3 h-6 w-6 ${fn:contains(uri, '/report/poissons') ? 'text-gray-500' : 'text-gray-400 group-hover:text-gray-500'}" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 17v2h6v-2M5 13h14l-1 8H6l-1-8zm4-9h6a2 2 0 012 2v5H7V6a2 2 0 012-2z" />
                </svg>
                Reporting Poissons
            </a>
        </div>
    </nav>
</div>
